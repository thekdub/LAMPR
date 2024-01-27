package com.thekdub

enum class LDAPOperationCode(val id: Int) {
    BIND_REQUEST(0),        // Used to initiate the authentication process.
    BIND_RESPONSE(1),       // A response to a Bind Request.
    UNBIND_REQUEST(2),      // Used to gracefully close the connection.
    SEARCH_REQUEST(3),      // Used to query the directory for entries matching given criteria.
    SEARCH_RES_ENTRY(4),    // Entry returned in response to a Search Request.
    SEARCH_RES_DONE(5),     // Indicates the completion of a Search Request.
    MODIFY_REQUEST(6),      // Used to request the modification of an entry.
    MODIFY_RESPONSE(7),     // A response to a Modify Request.
    ADD_REQUEST(8),         // Used to request to add a new entry.
    ADD_RESPONSE(9),        // A response to an Add Request.
    DEL_REQUEST(10),        // Used to request to delete an entry.
    DEL_RESPONSE(11),       // A response to a Delete Request.
    MODIFY_DN_REQUEST(12),  // Used to request to modify the DN of an entry.
    MODIFY_DN_RESPONSE(13), // A response to a Modify DN Request.
    COMPARE_REQUEST(14),    // Request to compare a given attribute value with values in an entry.
    COMPARE_RESPONSE(15),   // A response to a Compare Request.
    ABANDON_REQUEST(16),    // Used to abandon or cancel an outstanding operation.
    SEARCH_RES_REF(19),     // Reference returned in response to a Search Request.
    EXTENDED_REQUEST(23),   // Request for an extended operation.
    EXTENDED_RESPONSE(24);  // A response to an Extended Request.

    companion object {
        fun fromId(id: Int): LDAPOperationCode? {
            return entries.find { it.id == id }
        }
    }
}