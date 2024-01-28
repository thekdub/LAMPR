package com.thekdub.enums

/**
 * A filter that defines the conditions that must be fulfilled in order
 *    for the Search to match a given entry.
 *
 * The 'and', 'or', and 'not' choices can be used to form combinations
 *    of filters.  At least one filter element MUST be present in an 'and'
 *    or 'or' choice.  The others match against individual attribute values
 *    of entries in the scope of the Search.  (Implementor's note: the
 *    'not' filter is an example of a tagged choice in an implicitly-tagged
 *    module.  In BER this is treated as if the tag were explicit.)
 *
 * A server MUST evaluate filters according to the three-valued logic of
 *    [X.511] (1993), Clause 7.8.1.  In summary, a filter is evaluated to
 *    "TRUE", "FALSE", or "Undefined".  If the filter evaluates to TRUE for
 *    a particular entry, then the attributes of that entry are returned as
 *    part of the Search result (subject to any applicable access control
 *    restrictions).  If the filter evaluates to FALSE or Undefined, then
 *    the entry is ignored for the Search.
 *
 * A filter of the "and" choice is TRUE if all the filters in the SET OF
 *    evaluate to TRUE, FALSE if at least one filter is FALSE, and
 *    Undefined otherwise.  A filter of the "or" choice is FALSE if all the
 *    filters in the SET OF evaluate to FALSE, TRUE if at least one filter
 *    is TRUE, and Undefined otherwise.  A filter of the 'not' choice is
 *    TRUE if the filter being negated is FALSE, FALSE if it is TRUE, and
 *    Undefined if it is Undefined.
 *
 * A filter item evaluates to Undefined when the server would not be
 *    able to determine whether the assertion value matches an entry.
 *    Examples include:
 *
 * - An attribute description in an equalityMatch, substrings,
 *      greaterOrEqual, lessOrEqual, approxMatch, or extensibleMatch filter
 *      is not recognized by the server.
 *
 * - The attribute type does not define the appropriate matching rule.
 *
 * - A MatchingRuleId in the extensibleMatch is not recognized by the
 *      server or is not valid for the attribute type.
 *
 * - The type of filtering requested is not implemented.
 *
 * - The assertion value is invalid.
 *
 * For example, if a server did not recognize the attribute type
 *    shoeSize, the filters (shoeSize=*), (shoeSize=12), (shoeSize>=12),
 *    and (shoeSize<=12) would each evaluate to Undefined.
 *
 * Servers MUST NOT return errors if attribute descriptions or matching
 *    rule ids are not recognized, assertion values are invalid, or the
 *    assertion syntax is not supported.  More details of filter processing
 *    are given in Clause 7.8 of [X.511].
 * @see AND
 * @see OR
 * @see NOT
 * @see EQUALITY_MATCH
 * @see SUBSTRINGS
 * @see GREATER_OR_EQUAL
 * @see LESS_OR_EQUAL
 * @see PRESENT
 * @see APPROX_MATCH
 * @see EXTENSIBLE_MATCH
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7">RFC4511 Section 4.5.1.7</a>
 */
enum class LDAPFilterCode(val id: Int) {
    AND(0),
    OR(1),
    NOT(2),
    /**
     * The matching rule for an equalityMatch filter is defined by the
     *    EQUALITY matching rule for the attribute type or subtype.  The filter
     *    is TRUE when the EQUALITY rule returns TRUE as applied to the
     *    attribute or subtype and the asserted value.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.1">RFC4511 Section 4.5.1.7.1</a>
     */
    EQUALITY_MATCH(3),
    /**
     * There SHALL be at most one 'initial' and at most one 'final' in the
     *    'substrings' of a SubstringFilter.  If 'initial' is present, it SHALL
     *    be the first element of 'substrings'.  If 'final' is present, it
     *    SHALL be the last element of 'substrings'.
     *
     * The matching rule for an AssertionValue in a substrings filter item
     *    is defined by the SUBSTR matching rule for the attribute type or
     *    subtype.  The filter is TRUE when the SUBSTR rule returns TRUE as
     *    applied to the attribute or subtype and the asserted value.
     *
     * Note that the AssertionValue in a substrings filter item conforms to
     *    the assertion syntax of the EQUALITY matching rule for the attribute
     *    type rather than to the assertion syntax of the SUBSTR matching rule
     *    for the attribute type.  Conceptually, the entire SubstringFilter is
     *    converted into an assertion value of the substrings matching rule
     *    prior to applying the rule.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.2">RFC4511 Section 4.5.1.7.2</a>
     */
    SUBSTRINGS(4),
    /**
     * The matching rule for a greaterOrEqual filter is defined by the
     *    ORDERING matching rule for the attribute type or subtype.  The filter
     *    is TRUE when the ORDERING rule returns FALSE as applied to the
     *    attribute or subtype and the asserted value.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.3">RFC4511 Section 4.5.1.7.3</a>
     */
    GREATER_OR_EQUAL(5),
    /**
     * The matching rules for a lessOrEqual filter are defined by the
     *    ORDERING and EQUALITY matching rules for the attribute type or
     *    subtype.  The filter is TRUE when either the ORDERING or EQUALITY
     *    rule returns TRUE as applied to the attribute or subtype and the
     *    asserted value.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.4">RFC4511 Section 4.5.1.7.4</a>
     */
    LESS_OR_EQUAL(6),
    /**
     * A present filter is TRUE when there is an attribute or subtype of the
     *    specified attribute description present in an entry, FALSE when no
     *    attribute or subtype of the specified attribute description is
     *    present in an entry, and Undefined otherwise.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.5">RFC4511 Section 4.5.1.7.5</a>
     */
    PRESENT(7),
    /**
     * An approxMatch filter is TRUE when there is a value of the attribute
     *    type or subtype for which some locally-defined approximate matching
     *    algorithm (e.g., spelling variations, phonetic match, etc.) returns
     *    TRUE.  If a value matches for equality, it also satisfies an
     *    approximate match.  If approximate matching is not supported for the
     *    attribute, this filter item should be treated as an equalityMatch.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.6">RFC4511 Section 4.5.1.7.6</a>
     */
    APPROX_MATCH(8),
    /**
     * The fields of the extensibleMatch filter item are evaluated as
     *    follows:
     *
     *    - If the matchingRule field is absent, the type field MUST be
     *      present, and an equality match is performed for that type.
     *
     *    - If the type field is absent and the matchingRule is present, the
     *      matchValue is compared against all attributes in an entry that
     *      support that matchingRule.
     *
     *    - If the type field is present and the matchingRule is present, the
     *      matchValue is compared against the specified attribute type and its
     *      subtypes.
     *
     *    - If the dnAttributes field is set to TRUE, the match is additionally
     *      applied against all the AttributeValueAssertions in an entry's
     *      distinguished name, and it evaluates to TRUE if there is at least
     *      one attribute or subtype in the distinguished name for which the
     *      filter item evaluates to TRUE.  The dnAttributes field is present
     *      to alleviate the need for multiple versions of generic matching
     *      rules (such as word matching), where one applies to entries and
     *      another applies to entries and DN attributes as well.
     *
     * The matchingRule used for evaluation determines the syntax for the
     *    assertion value.  Once the matchingRule and attribute(s) have been
     *    determined, the filter item evaluates to TRUE if it matches at least
     *    one attribute type or subtype in the entry, FALSE if it does not
     *    match any attribute type or subtype in the entry, and Undefined if
     *    the matchingRule is not recognized, the matchingRule is unsuitable
     *    for use with the specified type, or the assertionValue is invalid.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.1.7.7">RFC4511 Section 4.5.1.7.7</a>
     */
    EXTENSIBLE_MATCH(9);

    companion object {
        /**
         * Converts an integer filter code into the appropriate enum value.
         * Returns null if an invalid code is provided.
         *
         * @param code The result code.
         * @return The corresponding filter enum, or null if not found.
         */
        fun fromId(code: Int): LDAPFilterCode? {
            return LDAPFilterCode.entries.find { it.id == code }
        }
    }
}