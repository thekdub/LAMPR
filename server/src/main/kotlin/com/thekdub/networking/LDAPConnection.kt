package com.thekdub.networking

import com.thekdub.enums.LDAPOperationCode
import com.thekdub.enums.LDAPResultCode
import com.thekdub.exceptions.InvalidRequestException
import com.thekdub.exceptions.MalformedRequestException
import com.thekdub.exceptions.UnsupportedActionException
import com.thekdub.networking.requests.*
import com.thekdub.networking.responses.LDAPBindResponse
import io.ktor.util.network.*
import org.bouncycastle.asn1.ASN1InputStream
import org.bouncycastle.asn1.ASN1Integer
import org.bouncycastle.asn1.ASN1Sequence
import org.bouncycastle.asn1.ASN1TaggedObject
import org.bouncycastle.asn1.util.ASN1Dump
import java.io.BufferedInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.Socket
import javax.net.SocketFactory

class LDAPConnection(
    private val socket: Socket
): Runnable {

    var id = nextID++
        private set

    private var nextMessageID: Int = 1
    private var bound: Boolean = false

    companion object {
        private var nextID: Int = 0
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
                    println("--- LDAP Connection [$id] New Data ---\n" +
                            "Raw: $berData\n" +
                            "Dump:\n${ASN1Dump.dumpAsString(berData)}")

                    if (berData is ASN1Sequence) {
                        val messageID = (berData.getObjectAt(0) as ASN1Integer).intValueExact()
                        nextMessageID = Math.max(nextMessageID, messageID + 1)
                        val baseRequest = LDAPBasicRequest(this, messageID)



                        try {
                            val message = parseMessage(this, berData)
                            println("Parsed Data: $message")

                            when (message) {
                                is LDAPBindRequest -> {
                                    //val response = LDAPBindResponse(this, message.messageID, LDAPResultCode.INVALID_CREDENTIALS, "TestA", "TestB")
                                    val response = LDAPBindResponse(this, message.messageID, LDAPResultCode.SUCCESS, null, null)
                                    // createFailureResponse(request?.messageID?: 0, 49)
                                    write(response.build())
                                }
                                is LDAPBindResponse -> {
                                    when (message.resultCode) {
                                        LDAPResultCode.SUCCESS -> {
                                            bound = true
                                            println("Bound to: ${message.matchedDN}")
                                        }
                                        else -> {
                                            bound = false
                                            println("Failed to bind: ${message.errorMessage}")
                                        }
                                    }
                                }
                                is LDAPUnbindRequest -> {
                                    println("Unhandled Unbind Request\n$message")
                                }
                                is LDAPSearchRequest -> {
                                    println("Unhandled Search Request\n$message")
                                }
                                is LDAPAbandonRequest -> {
                                    println("Unhandled Abandon Request\n$message")
                                }
                                else -> {
                                    socket.close()
                                    throw InvalidRequestException("Invalid request type.")
                                }

                            }
                        }
                        catch (e: UnsupportedActionException) {
                            write(
                                LDAPBindResponse(this,
                                messageID,
                                LDAPResultCode.UNWILLING_TO_PERFORM,
                                null,
                                "Unsupported Action."
                            ).build())
                            socket.close()
                        }
                        catch (e: InvalidRequestException) {
                            write(
                                LDAPBindResponse(this,
                                messageID,
                                LDAPResultCode.UNWILLING_TO_PERFORM,
                                null,
                                "Invalid Request."
                            ).build())
                            socket.close()
                        }
                        catch (e: MalformedRequestException) {
                            write(
                                LDAPBindResponse(this,
                                messageID,
                                LDAPResultCode.UNWILLING_TO_PERFORM,
                                null,
                                "Malformed Request."
                            ).build())
                            socket.close()
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

    private fun parseMessage(connection: LDAPConnection, request: ASN1Sequence): LDAPMessage? {
        val messageID = (request.getObjectAt(0) as ASN1Integer).intValueExact()
        val operation = request.getObjectAt(1) as ASN1TaggedObject
        val operationClass = operation.tagClass
        //println("OpClass: $operationClass")
        val operationID = operation.tagNo;
        return when (LDAPOperationCode.fromId(operationID)) {
            LDAPOperationCode.BIND_REQUEST -> LDAPBindRequest.fromASN1Sequence(connection, request)
            LDAPOperationCode.BIND_RESPONSE -> LDAPBindResponse.fromASN1Sequence(connection, request)
            LDAPOperationCode.UNBIND_REQUEST -> LDAPUnbindRequest.fromASN1Sequence(connection, request)
            LDAPOperationCode.SEARCH_REQUEST -> LDAPSearchRequest.fromASN1Sequence(connection, request)
            LDAPOperationCode.ABANDON_REQUEST -> LDAPAbandonRequest.fromASN1Sequence(connection, request)
            else -> {
                println("Invalid Request Code: ${operation.tagNo}\nRequest: $request")
                null
            }
        }
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