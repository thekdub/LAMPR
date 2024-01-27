package com.thekdub

import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.util.ASN1Dump
import org.bouncycastle.asn1.x500.X500Name
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

fun main() {
    val port = 389 // Change to a higher port for testing if necessary
    val serverSocket = ServerSocket(port)
    println("LDAP Server is listening on port $port")

    val threadPool = Executors.newWorkStealingPool(4)

    while (true) {
        val socket = serverSocket.accept()
        println()
        println("---- New client connected ----")
        println("\tIP: ${socket.inetAddress.hostAddress}")
        println("\tPort: ${socket.port} / ${socket.localPort}")

        threadPool.submit {
            handleClient(socket)
        }
    }
}

fun handleClient(socket: Socket) {
    try {
        val input = BufferedInputStream(socket.getInputStream())

        while (!socket.isClosed) {
            if (input.available() > 0) {
                val berData = ASN1InputStream(input).readObject()
                println()
                println("--- NEW DATA ---")
                println("Received data: $berData")
                println("Dump:\n${ASN1Dump.dumpAsString(berData)}")

                if (berData is ASN1Sequence) {
                    try {
                        val request = parseRequest(socket, berData)

                        if (request is LDAPBindRequest) {
                            val response = createSuccessResponse(request.messageID)
                            // createFailureResponse(request?.messageID?: 0, 49)
                            request.reply(response)
                        }
                        else {
                            socket.close()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    catch (e: IOException) {
        e.printStackTrace()
    }
    finally {
        socket.close()
    }
}

fun createSuccessResponse(messageId: Int): ByteArray {
    val response = ASN1EncodableVector()

    // Message ID
    response.add(ASN1Integer(messageId.toLong()))

    // BindResponse (Application Tag 1)
    val bindResponse = ASN1EncodableVector()

    // Result Code: success (0)
    bindResponse.add(ASN1Enumerated(LDAPResultCode.SUCCESS.id))

    // Matched DN: empty
    bindResponse.add(X500Name(""))

    // Diagnostic Message: empty
    bindResponse.add(DERUTF8String(""))

    // Add BindResponse to LDAPMessage
    response.add(DERTaggedObject(true, LDAPOperationCode.BIND_RESPONSE.id, DERSequence(bindResponse)))

    println("Response: ${DERSequence(response)}")

    return encodeASN1(DERSequence(response))
}

fun createFailureResponse(messageId: Int, resultCode: Int): ByteArray {
    val response = ASN1EncodableVector()

    // Message ID
    response.add(ASN1Integer(messageId.toLong()))

    // BindResponse (Application Tag 1)
    val bindResponse = ASN1EncodableVector()

    // Result Code: e.g., invalidCredentials (49)
    bindResponse.add(ASN1Enumerated(resultCode))

    // Matched DN: empty
    bindResponse.add(X500Name(""))

    // Diagnostic Message: empty or appropriate message
    bindResponse.add(DERUTF8String("Invalid credentials"))

    // Add BindResponse to LDAPMessage
    response.add(DERTaggedObject(true, 1, DERSequence(bindResponse)))

    return encodeASN1(DERSequence(response))
}

fun encodeASN1(encodable: ASN1Encodable): ByteArray {
    val bOut = ByteArrayOutputStream()
    val dOut = ASN1OutputStream.create(bOut, ASN1Encoding.DER)
    dOut.writeObject(encodable)
    dOut.close()
    return bOut.toByteArray()
}

fun parseRequest(socket: Socket, request: ASN1Sequence): LDAPRequest? {
    val messageID = request.getObjectAt(0) as ASN1Integer
    val operation = request.getObjectAt(1) as ASN1TaggedObject
    val operationID = operation.tagNo;
    when (LDAPOperationCode.fromId(operationID)) {
        LDAPOperationCode.BIND_REQUEST -> { // BindRequest
            val sequence = operation.baseObject as ASN1Sequence
            val ldapVersion = sequence.getObjectAt(0) as ASN1Integer
            val username = String((sequence.getObjectAt(1) as ASN1OctetString).octets, Charsets.US_ASCII)
            val passwordObject = sequence.getObjectAt(2) as ASN1TaggedObject
            when (passwordObject.tagNo) {
                0 -> {
                    val password = String((passwordObject.baseObject as ASN1OctetString).octets, Charsets.US_ASCII)
                    return LDAPBindRequest(socket,
                        messageID.intValueExact(),
                        ldapVersion.intValueExact(),
                        username,
                        password)
                }
                else -> {
                    println("Invalid Password Code: ${passwordObject.tagNo}\nRequest: $request")
                }
            }

        }
        LDAPOperationCode.UNBIND_REQUEST -> { // UnbindRequest
            val test = String((operation.baseObject as ASN1OctetString).octets, Charsets.US_ASCII);

            println("--- START UNBIND REQUEST ---")
            println("MessageID: $messageID")
            println("Operation: $operation")
            println("Test: $test")
            println("Dump: \n${ASN1Dump.dumpAsString(operation)}")
            println("--- END ---")


        }
        LDAPOperationCode.SEARCH_REQUEST -> { // Search Request
            val sequence = operation.baseObject as ASN1Sequence
            val baseDN = String((sequence.getObjectAt(0) as ASN1OctetString).octets, Charsets.US_ASCII)
            val scope = (sequence.getObjectAt(1) as ASN1Enumerated).intValueExact()
            // 0 - Base Object -- Just the entry named by baseDN
            // 1 - Single Level -- Just the immediate container named by baseDN
            // 2 - Whole Subtree -- Anything within or below baseDN
            val derefAliases = (sequence.getObjectAt(2) as ASN1Enumerated).intValueExact()
            // 0 - Never Deref Aliases -- Do not dereference aliases in searching
            // 1 - Deref in Searching -- While searching subordinates of the base DN, deref any alias within search scope. These become vertices of further search scopes where search operation is also applied; when scope is whole subtree, the search continues in subtrees of any deref object. If scope is single level, search is applied to any deref objects, and is not applied to subordinates. Servers should eliminate duplicate entries that arise due to alias dereferencing while searching.
            // 2 - Deref Finding Base Obj -- Deref aliases in locating the base object of search, but not when searching subordinates of the base object
            // 3 - Deref Always -- Deref aliases both in searching and in locating the base object of the search.
            val sizeLimit = sequence.getObjectAt(3) as ASN1Integer
            // 0 indicates no client specified limit; servers may also enforce their own limits.
            val timeLimit = sequence.getObjectAt(4) as ASN1Integer
            // 0 indicates no client specified limit; servers may also enforce their own limits.
            val typesOnly = sequence.getObjectAt(5) as ASN1Boolean
            // Indicator to if search results are to contain both attribute descriptions and values, or just attribute descriptions. TRUE causes ONLY attribute descriptions to be returned, not values. FALSE returns both.
            val filter = sequence.getObjectAt(6) as ASN1TaggedObject
            // https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7
            // Servers must evaluate filters to one of three: TRUE, FALSE, UNDEFINED.
            // TRUE -- The attributes of that entry are returned as part of the search result, subject to access control restrictions.
            // FALSE/UNDEFINED -- Entry is ignored for the search
            // Filter object evaluates to undefined when the server cannot determine whether the assertion value matches an entry. E.g. an attribute requested is unrecognized.
            val filterTag = filter.tagNo
            val filterObj = filter.baseObject as ASN1Sequence
            // 0 = And -- True if all filters in the set evaluate to true; false if at least one is false.
            // 1 = Or -- True if at least one filter in the set evaluates to true; false if none are true.
            // 2 = Not -- Negates the value of a filter.
            // 3 = Equal
            // 4 = Substrings -- Must be at most one 'initial' and at most one 'final' in substrings of substring filter. If initial is present, it shall be the first element of 'substrings', and 'final' if present the last.
            // 5 = Greater or Equal
            // 6 = Less or Equal
            // 7 = Present
            // 8 = Approx Match
            // 9 = Extensible Match

            // Substring Filter
            // 0 = Initial
            // 1 = Any
            // 2 = Final

            // Matching Rule Assertion
            // 1 = Matching Rule
            // 2 = Type
            // 3 = Match Value
            // 4 = DN Attributes
            val filterKey = String((filterObj.getObjectAt(0) as ASN1OctetString).octets, Charsets.US_ASCII)
            val filterValue = String((filterObj.getObjectAt(1) as ASN1OctetString).octets, Charsets.US_ASCII)
            val attributes = sequence.getObjectAt(7) as ASN1Sequence
            val attributeA = String((attributes.getObjectAt(0) as ASN1OctetString).octets, Charsets.US_ASCII)
            // Not so sure on this. https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.8

            println("Data:\n" +
                    "base DN: $baseDN\n" +
                    "Scope: $scope\n" +
                    "Deref Aliases: $derefAliases\n" +
                    "Size Limit: $sizeLimit\n" +
                    "Time Limit: $timeLimit\n" +
                    "Types Only: $typesOnly\n" +
                    "Filter: $filter\n" +
                    "filterTag: $filterTag\n" +
                    "FilterObjA: $filterKey\n" +
                    "FilterObjB: $filterValue\n" +
                    "AttributesA: $attributeA")

        }
        else -> {
            println("Invalid Request Code: ${operation.tagNo}\nRequest: $request")
        }
    }
    return null
}



interface LDAPRequest {
    val socket: Socket
    val messageID: Int
    val protocolVersion: Int

    fun reply(response: ByteArray)

}

class LDAPBindRequest(
    override val socket: Socket,
    override val messageID: Int,
    override val protocolVersion: Int,
    val username: String,
    val password: String
) : LDAPRequest {

    init {
        println(this)
    }

    override fun reply(response: ByteArray) {
        val out = DataOutputStream(socket.getOutputStream())
        out.write(response)
        out.flush()
    }

    override fun toString(): String {
        return "LDAPBindRequest={" +
                "socketIP: ${socket.inetAddress.hostAddress}, " +
                "socketPort: ${socket.localPort}, " +
                "messageID: $messageID, " +
                "protocolVersion: $protocolVersion, " +
                "username: $username, " +
                "password: $password}"
    }



}
