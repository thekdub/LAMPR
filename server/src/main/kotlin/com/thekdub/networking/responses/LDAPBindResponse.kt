package com.thekdub.networking.responses

import com.thekdub.enums.LDAPOperationCode
import com.thekdub.enums.LDAPResultCode
import com.thekdub.networking.LDAPConnection
import com.thekdub.utilities.Encoding
import org.bouncycastle.asn1.*
import kotlin.text.toByteArray

class LDAPBindResponse(
    connection: LDAPConnection,
    messageID: Int,
    val resultCode: LDAPResultCode,
    val matchedDN: String?,
    val errorMessage: String?
) : LDAPResponse(connection, messageID) {

    companion object {
        fun fromASN1Sequence(connection: LDAPConnection, data: ASN1Sequence): LDAPBindResponse {
            val messageID = (data.getObjectAt(0) as ASN1Integer).intValueExact()
            val operation = data.getObjectAt(1) as ASN1TaggedObject
            val sequence = operation.baseObject as ASN1Sequence
            val bindResponse = LDAPResultCode.fromId((sequence.getObjectAt(0) as ASN1Enumerated).intValueExact())
            val matchedDN = String((sequence.getObjectAt(1) as ASN1OctetString).octets, Charsets.US_ASCII)
            val errorMessage = String((sequence.getObjectAt(2) as ASN1OctetString).octets, Charsets.US_ASCII)
            return LDAPBindResponse(connection, messageID, bindResponse!!, matchedDN, errorMessage)
        }
    }

    override fun build(): ByteArray {
        val response = ASN1EncodableVector()
        response.add(ASN1Integer(messageID.toLong()))   // Message ID
        val bindResponse = ASN1EncodableVector()
        bindResponse.add(ASN1Enumerated(resultCode.id)) // Result Code
        bindResponse.add(DEROctetString((matchedDN?: "").toByteArray()))    // Matched DN
        bindResponse.add(DEROctetString((errorMessage?: "").toByteArray())) // Error Message
        response.add(DERTaggedObject(false, 64, LDAPOperationCode.BIND_RESPONSE.id, DERSequence(bindResponse)))
        return Encoding.asn1ToByteArray(DERSequence(response))
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID, " +
                "resultCode: $resultCode, " +
                "matchedDN: $matchedDN, " +
                "errorMessage: $errorMessage}"
    }

}