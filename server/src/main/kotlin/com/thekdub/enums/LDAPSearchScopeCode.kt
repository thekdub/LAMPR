package com.thekdub.enums

/**
 * Specifies the scope of the Search to be performed.  The semantics (as
 *    described in [X.511]) of the defined values of this field are:
 * @see BASE_OBJECT
 * @see SINGLE_LEVEL
 * @see WHOLE_SUBTREE
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.2">RFC4511 Section 4.5.1.2</a>
 */
enum class LDAPSearchScopeCode(val id: Int) {
    /**
     * The scope is constrained to the entry named by
     *       baseObject.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.2">RFC4511 Section 4.5.1.2</a>
     */
    BASE_OBJECT(0),
    /**
     * The scope is constrained to the immediate
     *       subordinates of the entry named by baseObject.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.2">RFC4511 Section 4.5.1.2</a>
     */
    SINGLE_LEVEL(1),
    /**
     * The scope is constrained to the entry named by
     *       baseObject and to all its subordinates.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.2">RFC4511 Section 4.5.1.2</a>
     */
    WHOLE_SUBTREE(2);

    companion object {
        /**
         * Converts an integer search scope code into the appropriate enum value.
         * Returns null if an invalid code is provided.
         *
         * @param code The result code.
         * @return The corresponding search scope enum, or null if not found.
         */
        fun fromId(code: Int): LDAPSearchScopeCode? {
            return LDAPSearchScopeCode.entries.find { it.id == code }
        }
    }
}