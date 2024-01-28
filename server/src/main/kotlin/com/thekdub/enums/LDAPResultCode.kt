package com.thekdub.enums

/**
 * The LDAPResult is the construct used in this protocol to return
 *    success or failure indications from servers to clients.  To various
 *    requests, servers will return responses containing the elements found
 *    in LDAPResult to indicate the final status of the protocol operation
 *    request.
 *
 * The resultCode enumeration is extensible as defined in Section 3.8 of
 *    [RFC4520].  The meanings of the listed result codes are given in
 *    Appendix A.  If a server detects multiple errors for an operation,
 *    only one result code is returned.  The server should return the
 *    result code that best indicates the nature of the error encountered.
 *    Servers may return substituted result codes to prevent unauthorized
 *    disclosures.
 *
 * The diagnosticMessage field of this construct may, at the server's
 *    option, be used to return a string containing a textual, human-
 *    readable diagnostic message (terminal control and page formatting
 *    characters should be avoided).  As this diagnostic message is not
 *    standardized, implementations MUST NOT rely on the values returned.
 *    Diagnostic messages typically supplement the resultCode with
 *    additional information.  If the server chooses not to return a
 *    textual diagnostic, the diagnosticMessage field MUST be empty.
 *
 * For certain result codes (typically, but not restricted to
 *    noSuchObject, aliasProblem, invalidDNSyntax, and
 *    aliasDereferencingProblem), the matchedDN field is set (subject to
 *    access controls) to the name of the last entry (object or alias) used
 *    in finding the target (or base) object.  This will be a truncated
 *    form of the provided name or, if an alias was dereferenced while
 *    attempting to locate the entry, of the resulting name.  Otherwise,
 *    the matchedDN field is empty.
 * @see SUCCESS
 * @see OPERATIONS_ERROR
 * @see PROTOCOL_ERROR
 * @see TIME_LIMIT_EXCEEDED
 * @see SIZE_LIMIT_EXCEEDED
 * @see COMPARE_FALSE
 * @see COMPARE_TRUE
 * @see AUTH_METHOD_NOT_SUPPORTED
 * @see STRONGER_AUTH_REQUIRED
 * @see REFERRAL
 * @see ADMIN_LIMIT_EXCEEDED
 * @see UNAVAILABLE_CRITICAL_EXTENSION
 * @see CONFIDENTIALITY_REQUIRED
 * @see SASL_BIND_IN_PROGRESS
 * @see NO_SUCH_ATTRIBUTE
 * @see INAPPROPRIATE_MATCHING
 * @see CONSTRAINT_VIOLATION
 * @see ATTRIBUTE_OR_VALUE_EXISTS
 * @see INVALID_ATTRIBUTE_SYNTAX
 * @see NO_SUCH_OBJECT
 * @see ALIAS_PROBLEM
 * @see INVALID_DN_SYNTAX
 * @see ALIAS_DEREFERENCING_PROBLEM
 * @see INAPPROPRIATE_AUTHENTICATION
 * @see INVALID_CREDENTIALS
 * @see INSUFFICIENT_ACCESS_RIGHTS
 * @see BUSY
 * @see UNAVAILABLE
 * @see UNWILLING_TO_PERFORM
 * @see LOOP_DETECT
 * @see NAMING_VIOLATION
 * @see OBJECT_CLASS_VIOLATION
 * @see NOT_ALLOWED_ON_NON_LEAF
 * @see NOT_ALLOWED_ON_RDN
 * @see ENTRY_ALREADY_EXISTS
 * @see OBJECT_CLASS_MODS_PROHIBITED
 * @see AFFECTS_MULTIPLE_DSAS
 * @see OTHER
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.1.9">RFC4511 Section 4.1.9</a>
 */
enum class LDAPResultCode(val id: Int) {
    SUCCESS(0),
    OPERATIONS_ERROR(1),
    PROTOCOL_ERROR(2),
    TIME_LIMIT_EXCEEDED(3),
    SIZE_LIMIT_EXCEEDED(4),
    COMPARE_FALSE(5),
    COMPARE_TRUE(6),
    AUTH_METHOD_NOT_SUPPORTED(7),
    STRONGER_AUTH_REQUIRED(8),
    REFERRAL(10),
    ADMIN_LIMIT_EXCEEDED(11),
    UNAVAILABLE_CRITICAL_EXTENSION(12),
    CONFIDENTIALITY_REQUIRED(13),
    SASL_BIND_IN_PROGRESS(14),
    NO_SUCH_ATTRIBUTE(16),
    UNDEFINED_ATTRIBUTE_TYPE(17),
    INAPPROPRIATE_MATCHING(18),
    CONSTRAINT_VIOLATION(19),
    ATTRIBUTE_OR_VALUE_EXISTS(20),
    INVALID_ATTRIBUTE_SYNTAX(21),
    NO_SUCH_OBJECT(32),
    ALIAS_PROBLEM(33),
    INVALID_DN_SYNTAX(34),
    ALIAS_DEREFERENCING_PROBLEM(36),
    INAPPROPRIATE_AUTHENTICATION(48),
    INVALID_CREDENTIALS(49),
    INSUFFICIENT_ACCESS_RIGHTS(50),
    BUSY(51),
    UNAVAILABLE(52),
    UNWILLING_TO_PERFORM(53),
    LOOP_DETECT(54),
    NAMING_VIOLATION(64),
    OBJECT_CLASS_VIOLATION(65),
    NOT_ALLOWED_ON_NON_LEAF(66),
    NOT_ALLOWED_ON_RDN(67),
    ENTRY_ALREADY_EXISTS(68),
    OBJECT_CLASS_MODS_PROHIBITED(69),
    AFFECTS_MULTIPLE_DSAS(71),
    OTHER(80);

    companion object {
        /**
         * Converts an integer result code into the appropriate enum value.
         * Returns null if an invalid code is provided.
         *
         * @param code The result code.
         * @return The corresponding result enum, or null if not found.
         */
        fun fromId(code: Int): LDAPResultCode? {
            return LDAPResultCode.entries.find { it.id == code }
        }
    }
}