package com.thekdub.responses

import com.thekdub.LDAPMessage
import com.thekdub.objects.LDAPConnection

abstract class LDAPResponse (
    val connection: LDAPConnection,
    val messageID: Int
): LDAPMessage {

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID}"
    }
}