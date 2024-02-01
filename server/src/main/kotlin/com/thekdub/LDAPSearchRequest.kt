package com.thekdub

import com.thekdub.enums.LDAPDerefAliasesCode
import com.thekdub.enums.LDAPSearchScopeCode
import java.net.Socket

class LDAPSearchRequest(
    socket: Socket,
    messageID: Int,
    val baseDN: String,
    val scope: LDAPSearchScopeCode,
    val derefAliases: LDAPDerefAliasesCode,
    val sizeLimit: Int,
    val timeLimit: Int,
    val typesOnly: Boolean,
    val filters: ArrayList<LDAPSearchFilter>,
    val attributes: ArrayList<LDAPSearchAttribute>
) : LDAPRequest(socket, messageID) {

    init {
        println(this)
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "socketIP: ${socket.inetAddress.hostAddress}, " +
                "socketPort: ${socket.localPort}, " +
                "messageID: $messageID, " +
                "baseDN: $baseDN, " +
                "scope: $scope, " +
                "derefAliases: $derefAliases, " +
                "sizeLimit: $sizeLimit, " +
                "timeLimit: $timeLimit, " +
                "typesOnly: $typesOnly, " +
                "filters: ${filters.joinToString(", ", "{", "}")}, " +
                "attributes: ${attributes.joinToString(", ", "{", "}")}}"
    }



}