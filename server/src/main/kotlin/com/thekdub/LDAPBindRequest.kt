package com.thekdub

import java.io.DataOutputStream
import java.net.Socket

class LDAPBindRequest(
    override val socket: Socket,
    override val messageID: Int,
    override val protocolVersion: Int,
    val username: String,
    val password: String
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
                "protocolVersion: $protocolVersion, " +
                "username: $username, " +
                "password: $password}"
    }



}