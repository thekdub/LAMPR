package com.thekdub.objects

import com.thekdub.enums.LDAPResultCode
import com.thekdub.exceptions.InvalidRequestException
import com.thekdub.exceptions.MalformattedRequestException
import com.thekdub.exceptions.UnsupportedActionException
import com.thekdub.parseRequest
import com.thekdub.requests.LDAPBasicRequest
import com.thekdub.requests.LDAPBindRequest
import com.thekdub.responses.LDAPBindResponse
import io.ktor.util.network.*
import org.bouncycastle.asn1.ASN1InputStream
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.util.ASN1Dump
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import javax.net.SocketFactory

class LDAPConnection(
    private val socket: Socket
): Runnable {

    private var nextMessageID: Int = 1
    private var isAuthenticated: Boolean = false

    companion object {
        fun create(host: String, port: Int): LDAPConnection {
            return LDAPConnection(SocketFactory.getDefault().createSocket(host, port))
        }
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
        try {
            val input = BufferedInputStream(socket.getInputStream())

            while (!socket.isClosed) {
                if (input.available() > 0) {
                    val berData = ASN1InputStream(input).readObject()

                    println()
                    println("--- NEW DATA ---")
                    println("Received data: $berData")
                    println("Dump:\n${ASN1Dump.dumpAsString(berData)}")

                    if (berData is ASN1Sequence) {
                        val messageID = (berData.getObjectAt(0) as ASN1Integer).intValueExact()
                        nextMessageID = Math.max(nextMessageID, messageID + 1)
                        val baseRequest = LDAPBasicRequest(this, messageID)

                        try {
                            val message = parseRequest(this, berData)
                            println("Parsed Data: $message")

                            if (message is LDAPBindRequest) {
                                val response = LDAPBindResponse(this, message.messageID, LDAPResultCode.SUCCESS, null, null)
                                // createFailureResponse(request?.messageID?: 0, 49)
                                write(response.build())
                            }
                            else {
                                socket.close()
                            }

                        }
                        catch (eae: UnsupportedActionException) {
                            write(LDAPBindResponse(this,
                                messageID,
                                LDAPResultCode.UNWILLING_TO_PERFORM,
                                null,
                                null
                            ).build())
                        }
                        catch (ire: InvalidRequestException) {
                            write(LDAPBindResponse(this,
                                messageID,
                                LDAPResultCode.UNWILLING_TO_PERFORM,
                                null,
                                null
                            ).build())
                        }
                        catch (mre: MalformattedRequestException) {
                            write(LDAPBindResponse(this,
                                messageID,
                                LDAPResultCode.UNWILLING_TO_PERFORM,
                                null,
                                null
                            ).build())
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        finally {
            socket.close()
        }
    }

    fun write(data: ByteArray) {
        println("Transmitting: $data")
        val out = DataOutputStream(socket.getOutputStream())
        out.write(data)
        out.flush()
    }

    fun getLocalAddress(): String? {
        return socket.localAddress.hostAddress
    }

    fun getRemoteAddress(): String {
        return socket.remoteSocketAddress.address
    }

    fun getLocalPort(): Int {
        return socket.localPort
    }

    fun getRemotePort(): Int {
        return socket.remoteSocketAddress.port
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "localAddress: ${getLocalAddress()}, " +
                "remoteAddress: ${getRemoteAddress()}, " +
                "localPort: ${getLocalPort()}, " +
                "remotePort: ${getRemotePort()}, " +
                "connected: ${socket.isConnected}}"
    }

}