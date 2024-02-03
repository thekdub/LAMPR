package com.thekdub.requests

import com.thekdub.LDAPMessage
import com.thekdub.objects.LDAPConnection

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