package com.thekdub.networking.responses

import com.thekdub.networking.LDAPConnection
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Sequence

class LDAPSearchResponse(
    connection: LDAPConnection,
    messageID: Int
) : LDAPResponse(connection, messageID) {

    companion object {
        fun fromASN1Sequence(connection: LDAPConnection, data: ASN1Sequence): LDAPSearchResponse {
            val messageID = (data.getObjectAt(0) as ASN1Integer).intValueExact()
            return LDAPSearchResponse(connection, messageID)
        }
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