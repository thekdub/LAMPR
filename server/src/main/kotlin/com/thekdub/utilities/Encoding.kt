package com.thekdub.utilities

import org.bouncycastle.asn1.ASN1Encodable
import org.bouncycastle.asn1.ASN1Encoding
import org.bouncycastle.asn1.ASN1OutputStream
import java.io.ByteArrayOutputStream

class Encoding {

    companion object {
        fun asn1ToByteArray(data: ASN1Encodable): ByteArray {
            val bOut = ByteArrayOutputStream()
            val dOut = ASN1OutputStream.create(bOut, ASN1Encoding.DER)
            dOut.writeObject(data)
            dOut.close()
            return bOut.toByteArray()
        }
    }
}