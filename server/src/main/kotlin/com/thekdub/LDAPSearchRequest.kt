package com.thekdub

import com.thekdub.enums.LDAPDerefAliasesCode
import com.thekdub.enums.LDAPSearchScopeCode
import java.io.DataOutputStream
import java.net.Socket

class LDAPSearchRequest(
    override val socket: Socket,
    override val messageID: Int,
    override val protocolVersion: Int,
    val baseDN: String,
    val scope: LDAPSearchScopeCode,
    val derefAliases: LDAPDerefAliasesCode,
    val sizeLimit: Int,
    val timeLimit: Int,
    val typesOnly: Boolean
) : LDAPRequest {

    init {
        println(this)
    }

    override fun reply(response: ByteArray) {
        val out = DataOutputStream(socket.getOutputStream())
        out.write(response)
        out.flush()
    }

    override fun toString(): String {
        return "LDAPBindRequest={" +
                "socketIP: ${socket.inetAddress.hostAddress}, " +
                "socketPort: ${socket.localPort}, " +
                "messageID: $messageID, " +
                "protocolVersion: $protocolVersion}"
    }



}