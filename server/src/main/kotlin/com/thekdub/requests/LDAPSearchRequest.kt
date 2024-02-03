package com.thekdub.requests

import com.thekdub.objects.LDAPConnection
import com.thekdub.objects.LDAPSearchAttribute
import com.thekdub.objects.LDAPSearchFilter
import com.thekdub.enums.LDAPDerefAliasesCode
import com.thekdub.enums.LDAPSearchScopeCode

class LDAPSearchRequest(
    connection: LDAPConnection,
    messageID: Int,
    val baseDN: String,
    val scope: LDAPSearchScopeCode,
    val derefAliases: LDAPDerefAliasesCode,
    val sizeLimit: Int,
    val timeLimit: Int,
    val typesOnly: Boolean,
    val filters: ArrayList<LDAPSearchFilter>,
    val attributes: ArrayList<LDAPSearchAttribute>
) : LDAPRequest(connection, messageID) {

    init {
        println(this)
    }

    override fun build(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "${javaClass.name}={" +
                "connection: $connection, " +
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