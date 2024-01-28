package com.thekdub.enums

/**
 * An indicator as to whether or not alias entries (as defined in
 *    [RFC4512]) are to be dereferenced during stages of the Search
 *    operation.
 *
 * The act of dereferencing an alias includes recursively dereferencing
 *    aliases that refer to aliases.
 *
 * Servers MUST detect looping while dereferencing aliases in order to
 *    prevent denial-of-service attacks of this nature.
 * @see NEVER_DEREF_ALIASES
 * @see DEREF_IN_SEARCHING
 * @see DEREF_FINDING_BASE_OBJ
 * @see DEREF_ALWAYS
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.3">RFC4511 Section 4.5.1.3</a>
 */
enum class LDAPDerefAliasesCode(val id: Int) {
    /**
     * Do not dereference aliases in searching or in
     *       locating the base object of the Search.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.3">RFC4511 Section 4.5.1.3</a>
     */
    NEVER_DEREF_ALIASES(0),
    /**
     * While searching subordinates of the base object,
     *       dereference any alias within the search scope.  Dereferenced
     *       objects become the vertices of further search scopes where the
     *       Search operation is also applied.  If the search scope is
     *       wholeSubtree, the Search continues in the subtree(s) of any
     *       dereferenced object.  If the search scope is singleLevel, the
     *       search is applied to any dereferenced objects and is not applied
     *       to their subordinates.  Servers SHOULD eliminate duplicate entries
     *       that arise due to alias dereferencing while searching.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.3">RFC4511 Section 4.5.1.3</a>
     */
    DEREF_IN_SEARCHING(1),
    /**
     * Dereference aliases in locating the base
     *       object of the Search, but not when searching subordinates of the
     *       base object.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.3">RFC4511 Section 4.5.1.3</a>
     */
    DEREF_FINDING_BASE_OBJ(2),
    /**
     * Dereference aliases both in searching and in locating
     *       the base object of the Search.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.3">RFC4511 Section 4.5.1.3</a>
     */
    DEREF_ALWAYS(3);

    companion object {
        /**
         * Converts an integer deref aliases code into the appropriate enum value.
         * Returns null if an invalid code is provided.
         *
         * @param code The result code.
         * @return The corresponding deref aliases enum, or null if not found.
         */
        fun fromId(code: Int): LDAPDerefAliasesCode? {
            return LDAPDerefAliasesCode.entries.find { it.id == code }
        }
    }
}