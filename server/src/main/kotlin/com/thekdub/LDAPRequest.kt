package com.thekdub

import java.io.DataOutputStream
import java.net.Socket

abstract class LDAPRequest(
    val socket: Socket,
    val messageID: Int
) {

    fun reply(response: ByteArray) {
        println("Reply: $response")
        val out = DataOutputStream(socket.getOutputStream())
        out.write(response)
        out.flush()
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "socketIP: ${socket.inetAddress.hostAddress}, " +
                "socketPort: ${socket.localPort}, " +
                "messageID: $messageID}"
    }

}