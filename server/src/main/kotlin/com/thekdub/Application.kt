package com.thekdub

import com.thekdub.enums.*
import com.thekdub.objects.LDAPConnection
import com.thekdub.objects.LDAPSearchAttribute
import com.thekdub.objects.LDAPSearchFilter
import com.thekdub.requests.LDAPBindRequest
import com.thekdub.requests.LDAPRequest
import com.thekdub.requests.LDAPSearchRequest
import com.thekdub.responses.LDAPBindResponse
import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.util.ASN1Dump
import java.net.ServerSocket
import java.util.concurrent.Executors

fun main() {
    val port = 389 // Change to a higher port for testing if necessary
    val serverSocket = ServerSocket(port)
    println("LDAP Server is listening on port $port")

    val threadPool = Executors.newWorkStealingPool(4)

    // Test connection to AD server
//    val testClient = LDAPConnection.create("192.168.1.80", 389)
//    threadPool.submit(testClient)
//    testClient.write(LDAPBindRequest(testClient, 1, 3, "cn=lampr,ou=service accounts,ou=company,dc=company1,dc=local", "xpDkY4B5Zw94").build())
    // End AD test

    while (true) {
        val socket = serverSocket.accept()

        val connection = LDAPConnection(socket)

        println()
        println("---- New client connected ----")
        println("\tIP: ${socket.inetAddress.hostAddress}")
        println("\tPort: ${socket.port} / ${socket.localPort}")

        threadPool.submit(connection)
    }
}


fun parseRequest(connection: LDAPConnection, request: ASN1Sequence): LDAPMessage? {
    val messageID = (request.getObjectAt(0) as ASN1Integer).intValueExact()
    val operation = request.getObjectAt(1) as ASN1TaggedObject
    val operationClass = operation.tagClass
    println("OpClass: $operationClass")
    val operationID = operation.tagNo;
    when (LDAPOperationCode.fromId(operationID)) {
        LDAPOperationCode.BIND_REQUEST -> { // BindRequest
            val sequence = operation.baseObject as ASN1Sequence
            val ldapVersion = (sequence.getObjectAt(0) as ASN1Integer).intValueExact()
            val username = String((sequence.getObjectAt(1) as ASN1OctetString).octets, Charsets.US_ASCII)
            val passwordObject = sequence.getObjectAt(2) as ASN1TaggedObject
            when (passwordObject.tagNo) {
                0 -> {
                    val password = String((passwordObject.baseObject as ASN1OctetString).octets, Charsets.US_ASCII)
                    return LDAPBindRequest(connection,
                        messageID,
                        ldapVersion,
                        username,
                        password)
                }
                else -> {
                    println("Invalid Password Code: ${passwordObject.tagNo}\nRequest: $request")
                }
            }
        }
        LDAPOperationCode.BIND_RESPONSE -> {
            return LDAPBindResponse(connection, messageID, LDAPResultCode.SUCCESS, null, null)
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

            // [2, [APPLICATION 3][#, org.bouncycastle.asn1.ASN1Enumerated@202, org.bouncycastle.asn1.ASN1Enumerated@202, 0, 5, FALSE, [CONTEXT 7]#6f626a656374436c617373, [#737562736368656d61537562656e747279]]]
            /*
            Sequence
                Integer(2)
                Tagged [APPLICATION 3] IMPLICIT
                    Sequence
                        DER Octet String[0]
                        DER Enumerated(0)
                        DER Enumerated(0)
                        Integer(0)
                        Integer(5)
                        Boolean(false)
                        Tagged [CONTEXT 7] IMPLICIT
                            DER Octet String[11]
                        Sequence
                            DER Octet String[17]
            Data:
            base DN:
            Scope: BASE_OBJECT
            Deref Aliases: NEVER_DEREF_ALIASES
            Size Limit: 0
            Time Limit: 5
            Types Only: false
            Filter: [CONTEXT 7]#6f626a656374436c617373
            filterTag: PRESENT
            Attributes: [com.thekdub.objects.LDAPSearchAttribute={value: subschemaSubentry}]


             */


            val sequence = operation.baseObject as ASN1Sequence
            val baseDN = String((sequence.getObjectAt(0) as ASN1OctetString).octets, Charsets.US_ASCII)
            val scope = LDAPSearchScopeCode.fromId((sequence.getObjectAt(1) as ASN1Enumerated).intValueExact())
            val derefAliases = LDAPDerefAliasesCode.fromId((sequence.getObjectAt(2) as ASN1Enumerated).intValueExact())
            val sizeLimit = (sequence.getObjectAt(3) as ASN1Integer).intValueExact()
            // 0 indicates no client specified limit; servers may also enforce their own limits.
            val timeLimit = (sequence.getObjectAt(4) as ASN1Integer).intValueExact()
            // 0 indicates no client specified limit; servers may also enforce their own limits.
            val typesOnly = (sequence.getObjectAt(5) as ASN1Boolean).isTrue
            // Indicator to if search results are to contain both attribute descriptions and values, or just attribute descriptions. TRUE causes ONLY attribute descriptions to be returned, not values. FALSE returns both.
            val filter = sequence.getObjectAt(6) as ASN1TaggedObject
            // https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7
            // Servers must evaluate filters to one of three: TRUE, FALSE, UNDEFINED.
            // TRUE -- The attributes of that entry are returned as part of the search result, subject to access control restrictions.
            // FALSE/UNDEFINED -- Entry is ignored for the search
            // Filter object evaluates to undefined when the server cannot determine whether the assertion value matches an entry. E.g. an attribute requested is unrecognized.
            val filterTag = LDAPFilterCode.fromId(filter.tagNo)
            val filterObj = filter.baseObject as DEROctetString
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
//            val filterKey = String((filterObj.getObjectAt(0) as ASN1OctetString).octets, Charsets.US_ASCII)
//            val filterValue = String((filterObj.getObjectAt(1) as ASN1OctetString).octets, Charsets.US_ASCII)
            val filters = ArrayList<LDAPSearchFilter>()

            println("Filter Object: ${String(filterObj.octets, Charsets.US_ASCII)}")

//            filters.add(LDAPSearchFilter(filterTag!!, filterKey, filterValue))

            val attributes = ArrayList<LDAPSearchAttribute>()

            for (attribute in (sequence.getObjectAt(7) as ASN1Sequence).objects) {
                attributes.add(LDAPSearchAttribute(String((attribute as ASN1OctetString).octets, Charsets.US_ASCII)))
            }
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
//                    "FilterObjA: $filterKey\n" +
//                    "FilterObjB: $filterValue\n" +
                    "Attributes: $attributes")

            return LDAPSearchRequest(connection,
                messageID,
                baseDN,
                scope!!,
                derefAliases!!,
                sizeLimit,
                timeLimit,
                typesOnly,
                filters,
                attributes)
        }
        else -> {
            println("Invalid Request Code: ${operation.tagNo}\nRequest: $request")
        }
    }
    return null
}



