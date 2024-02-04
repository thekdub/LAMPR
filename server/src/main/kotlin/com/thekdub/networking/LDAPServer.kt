package com.thekdub.networking

import java.net.ServerSocket
import java.util.concurrent.Executors

class LDAPServer(
    private val port: Int
): Thread() {
    var id = nextID++
        private set

    companion object {
        private var nextID = 0;
    }

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
        println("-- LDAP Server [$id] Started @ Port $port --")
        val server = ServerSocket(port)
        val threadPool = Executors.newWorkStealingPool(4)
        while (true) {
            val socket = server.accept()
            val connection = LDAPConnection(socket)
            threadPool.submit(connection)
            println("-- LDAP Server [$id] Connection [${connection.id}] --\n" +
                    "IP: ${socket.inetAddress.hostAddress}\n" +
                    "Port: ${socket.port} / ${socket.localPort}")
        }
    }

}