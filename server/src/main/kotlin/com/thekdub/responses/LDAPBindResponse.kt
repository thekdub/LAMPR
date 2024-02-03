package com.thekdub.responses

import com.thekdub.enums.LDAPOperationCode
import com.thekdub.enums.LDAPResultCode
import com.thekdub.objects.LDAPConnection
import com.thekdub.utilities.Encoding
import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.x500.X500Name
import kotlin.text.toByteArray

class LDAPBindResponse(
    connection: LDAPConnection,
    messageID: Int,
    val resultCode: LDAPResultCode,
    val matchedDN: String?,
    val errorMessage: String?
) : LDAPResponse(connection, messageID) {

    override fun build(): ByteArray {
        val response = ASN1EncodableVector()
        // Message ID
        response.add(ASN1Integer(messageID.toLong()))
        // BindResponse (Application Tag 1)
        val bindResponse = ASN1EncodableVector()
        // Result Code: success (0)
        bindResponse.add(ASN1Enumerated(resultCode.id))
        // Matched DN: empty
        bindResponse.add(DEROctetString((matchedDN?: "").toByteArray()))
        // Diagnostic Message: empty
        bindResponse.add(DEROctetString((errorMessage?: "").toByteArray()))
        // Add BindResponse to LDAPMessage
        response.add(DERTaggedObject(false, 64, LDAPOperationCode.BIND_RESPONSE.id, DERSequence(bindResponse)))

        println("Response: ${DERSequence(response)}")

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