package com.thekdub.networking.responses

import com.thekdub.networking.LDAPMessage
import com.thekdub.networking.LDAPConnection

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