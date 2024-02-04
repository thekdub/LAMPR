package com.thekdub.networking.requests

import com.thekdub.networking.LDAPMessage
import com.thekdub.networking.LDAPConnection

abstract class LDAPRequest(
    val connection: LDAPConnection,
    val messageID: Int
): LDAPMessage {

    abstract override fun build(): ByteArray

    override fun toString(): String {
        return "${javaClass.name}={" +
                "messageID: $messageID}"
    }
}