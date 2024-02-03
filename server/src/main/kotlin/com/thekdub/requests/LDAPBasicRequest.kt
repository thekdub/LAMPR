package com.thekdub.requests

import com.thekdub.objects.LDAPConnection

class LDAPBasicRequest(
    connection: LDAPConnection,
    messageID: Int
) : LDAPRequest(connection, messageID) {

    init {
        println(this)
    }

    override fun build(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID}"
    }

}