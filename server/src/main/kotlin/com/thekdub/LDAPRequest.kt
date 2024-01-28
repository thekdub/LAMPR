package com.thekdub

import java.net.Socket

interface LDAPRequest {
    val socket: Socket
    val messageID: Int
    val protocolVersion: Int

    fun reply(response: ByteArray)

}