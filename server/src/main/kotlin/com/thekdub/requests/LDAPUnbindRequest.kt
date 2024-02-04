package com.thekdub.requests

import com.thekdub.enums.LDAPOperationCode
import com.thekdub.exceptions.InvalidRequestException
import com.thekdub.objects.LDAPConnection
import org.bouncycastle.asn1.*
import kotlin.text.toByteArray

class LDAPUnbindRequest(
    connection: LDAPConnection,
    messageID: Int
) : LDAPRequest(connection, messageID) {

    companion object {
        fun fromASN1Sequence(connection: LDAPConnection, data: ASN1Sequence): LDAPUnbindRequest {
            val messageID = (data.getObjectAt(0) as ASN1Integer).intValueExact()
            return LDAPUnbindRequest(connection, messageID)
        }
    }

    override fun build(): ByteArray {
        // Operation Data
        val operationTag = DERTaggedObject(
            false,
            64,
            LDAPOperationCode.UNBIND_REQUEST.id,
            null)
        // Packet wrapper
        val sequence = DERSequence(arrayOf(ASN1Integer(messageID.toLong()), operationTag))
        println(sequence)
        // Encode to bytes
        return sequence.encoded
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID}"
    }

}