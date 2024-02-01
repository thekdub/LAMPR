package com.thekdub

import java.net.Socket

class LDAPBasicRequest(socket: Socket, messageID: Int) : LDAPRequest(socket, messageID) {

    init {
        println(this)
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "socketIP: ${socket.inetAddress.hostAddress}, " +
                "socketPort: ${socket.localPort}, " +
                "messageID: $messageID}"
    }

}