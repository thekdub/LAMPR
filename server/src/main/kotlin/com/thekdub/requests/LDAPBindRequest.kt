package com.thekdub.requests

import com.thekdub.enums.LDAPOperationCode
import com.thekdub.objects.LDAPConnection
import com.thekdub.utilities.Encoding
import io.ktor.utils.io.core.*
import org.bouncycastle.asn1.*
import org.bouncycastle.asn1.util.ASN1Dump
import kotlin.text.toByteArray

class LDAPBindRequest(
    connection: LDAPConnection,
    messageID: Int,
    private val protocolVersion: Int,
    private val username: String,
    private val password: String
) : LDAPRequest(connection, messageID) {

    init {
        println(this)
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