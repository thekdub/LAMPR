package com.thekdub

import java.io.DataOutputStream
import java.net.Socket

class LDAPBindRequest(
    socket: Socket,
    messageID: Int,
    private val protocolVersion: Int,
    private val username: String,
    private val password: String
) : LDAPRequest(socket, messageID) {

    init {
        println(this)
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "socketIP: ${socket.inetAddress.hostAddress}, " +
                "socketPort: ${socket.localPort}, " +
                "messageID: $messageID, " +
                "protocolVersion: $protocolVersion, " +
                "username: $username, " +
                "password: $password}"
    }



}