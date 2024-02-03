package com.thekdub.responses

import com.thekdub.objects.LDAPConnection

// todo: Change this to actually represent a search response!
class LDAPSearchResponse(
    connection: LDAPConnection,
    messageID: Int
) : LDAPResponse(connection, messageID) {
    override fun build(): ByteArray {
        TODO("Not yet implemented")
    }
    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID}"
    }


}