package com.thekdub

import com.thekdub.networking.LDAPServer

fun main() {
    val ldapServer = LDAPServer(389)
    ldapServer.start()

    // Test connection to AD server
//    val testClient = LDAPConnection.create("192.168.1.80", 389)
//    threadPool.submit(testClient)
//    testClient.write(LDAPBindRequest(testClient, 1, 3, "cn=lampr,ou=service accounts,ou=company,dc=company1,dc=local", "xpDkY4B5Zw94").build())
    // End AD test
}




