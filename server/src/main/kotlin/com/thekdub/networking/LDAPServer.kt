package com.thekdub.networking

import java.net.ServerSocket
import java.util.concurrent.Executors

class LDAPServer(
    private val port: Int
): Thread() {

    /**
     * When an object implementing interface `Runnable` is used
     * to create a thread, starting the thread causes the object's
     * `run` method to be called in that separately executing
     * thread.
     *
     *
     * The general contract of the method `run` is that it may
     * take any action whatsoever.
     *
     * @see java.lang.Thread.run
     */
    override fun run() {
        println("Started LDAP Server on port $port")
        val server = ServerSocket(port)

        val threadPool = Executors.newWorkStealingPool(4)
        while (true) {
            val socket = server.accept()

            val connection = LDAPConnection(socket)

            println()
            println("---- New client connected ----")
            println("\tIP: ${socket.inetAddress.hostAddress}")
            println("\tPort: ${socket.port} / ${socket.localPort}")

            threadPool.submit(connection)
        }
    }

}