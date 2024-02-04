package com.thekdub.networking.requests

import com.thekdub.enums.LDAPOperationCode
import com.thekdub.exceptions.InvalidRequestException
import com.thekdub.networking.LDAPConnection
import org.bouncycastle.asn1.*
import kotlin.text.toByteArray

class LDAPBindRequest(
    connection: LDAPConnection,
    messageID: Int,
    private val protocolVersion: Int,
    private val username: String,
    private val password: String
) : LDAPRequest(connection, messageID) {

    companion object {
        fun fromASN1Sequence(connection: LDAPConnection, data: ASN1Sequence): LDAPBindRequest {
            val messageID = (data.getObjectAt(0) as ASN1Integer).intValueExact()
            val operation = data.getObjectAt(1) as ASN1TaggedObject
            val operationClass = operation.tagClass
            val operationID = operation.tagNo;
            val sequence = operation.baseObject as ASN1Sequence
            val ldapVersion = (sequence.getObjectAt(0) as ASN1Integer).intValueExact()
            val username = String((sequence.getObjectAt(1) as ASN1OctetString).octets, Charsets.US_ASCII)
            val passwordObject = sequence.getObjectAt(2) as ASN1TaggedObject
            when (passwordObject.tagNo) {
                0 -> {
                    val password = String((passwordObject.baseObject as ASN1OctetString).octets, Charsets.US_ASCII)
                    return LDAPBindRequest(
                        connection,
                        messageID,
                        ldapVersion,
                        username,
                        password
                    )
                }
                else -> {
                    println("Invalid Password Code: ${passwordObject.tagNo}\nRequest: $data")
                }
            }
            throw InvalidRequestException("An unexpected error occurred while parsing this request!")
        }
    }

    override fun build(): ByteArray {
        val passwordOctStr = DEROctetString(password.toByteArray())
        val passwordContext = DERTaggedObject(false, 0, passwordOctStr)
        val credentialSequence = DERSequence(arrayOf(
            ASN1Integer(protocolVersion.toLong()), // Version
            DEROctetString(username.toByteArray()), // Username Oct
            passwordContext)) // Password Oct
        // Operation Data
        val operationTag = DERTaggedObject(
            false,
            64,
            LDAPOperationCode.BIND_REQUEST.id,
            credentialSequence)
        // Packet wrapper
        val sequence = DERSequence(arrayOf(ASN1Integer(messageID.toLong()), operationTag))
        println(sequence)
        // Encode to bytes
        return sequence.encoded
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
                "messageID: $messageID, " +
                "protocolVersion: $protocolVersion, " +
                "username: $username, " +
                "password: $password}"
    }

}