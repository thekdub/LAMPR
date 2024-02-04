package com.thekdub

import com.thekdub.enums.*
import com.thekdub.objects.LDAPConnection
import com.thekdub.objects.LDAPSearchAttribute
import com.thekdub.objects.LDAPSearchFilter
import com.thekdub.requests.LDAPBindRequest
import com.thekdub.requests.LDAPRequest
import com.thekdub.requests.LDAPSearchRequest
import com.thekdub.requests.LDAPUnbindRequest
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
    //println("OpClass: $operationClass")
    val operationID = operation.tagNo;
    return when (LDAPOperationCode.fromId(operationID)) {
        LDAPOperationCode.BIND_REQUEST -> LDAPBindRequest.fromASN1Sequence(connection, request)
        LDAPOperationCode.BIND_RESPONSE -> LDAPBindResponse.fromASN1Sequence(connection, request)
        LDAPOperationCode.UNBIND_REQUEST -> LDAPUnbindRequest.fromASN1Sequence(connection, request)
        LDAPOperationCode.SEARCH_REQUEST -> LDAPSearchRequest.fromASN1Sequence(connection, request)
        else -> {
            println("Invalid Request Code: ${operation.tagNo}\nRequest: $request")
            null
        }
    }
}



