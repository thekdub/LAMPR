package com.thekdub.networking.requests

import com.thekdub.networking.LDAPConnection
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Sequence

class LDAPBasicRequest(
    connection: LDAPConnection,
    messageID: Int
) : LDAPRequest(connection, messageID) {

    override fun build(): ByteArray {
        TODO("Not yet implemented")
    }

    companion object {
        fun fromASN1Sequence(connection: LDAPConnection, data: ASN1Sequence): LDAPBasicRequest {
            val messageID = (data.getObjectAt(0) as ASN1Integer).intValueExact()
            return LDAPBasicRequest(connection, messageID)
        }
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID}"
    }

}