package com.thekdub.enums

/**
 * Controls provide a mechanism whereby the semantics and arguments of
 *    existing LDAP operations may be extended.  One or more controls may
 *    be attached to a single LDAP message.  A control only affects the
 *    semantics of the message it is attached to.
 *
 * Controls sent by clients are termed 'request controls', and those
 *    sent by servers are termed 'response controls'.
 *
 *         Controls ::= SEQUENCE OF control Control
 *
 *         Control ::= SEQUENCE {
 *              controlType             LDAPOID,
 *              criticality             BOOLEAN DEFAULT FALSE,
 *              controlValue            OCTET STRING OPTIONAL }
 *
 * The controlType field is the dotted-decimal representation of an
 *    OBJECT IDENTIFIER that uniquely identifies the control.  This
 *    provides unambiguous naming of controls.  Often, response control(s)
 *    solicited by a request control share controlType values with the
 *    request control.
 *
 * The criticality field only has meaning in controls attached to
 *    request messages (except UnbindRequest).  For controls attached to
 *    response messages and the UnbindRequest, the criticality field SHOULD
 *    be FALSE, and MUST be ignored by the receiving protocol peer.  A
 *    value of TRUE indicates that it is unacceptable to perform the
 *    operation without applying the semantics of the control.
 *    Specifically, the criticality field is applied as follows:
 *
 * - If the server does not recognize the control type, determines that
 *      it is not appropriate for the operation, or is otherwise unwilling
 *      to perform the operation with the control, and if the criticality
 *      field is TRUE, the server MUST NOT perform the operation, and for
 *      operations that have a response message, it MUST return with the
 *      resultCode set to unavailableCriticalExtension.
 *
 * - If the server does not recognize the control type, determines that
 *      it is not appropriate for the operation, or is otherwise unwilling
 *      to perform the operation with the control, and if the criticality
 *      field is FALSE, the server MUST ignore the control.
 *
 * - Regardless of criticality, if a control is applied to an
 *      operation, it is applied consistently and impartially to the
 *      entire operation.
 *
 * The controlValue may contain information associated with the
 *    controlType.  Its format is defined by the specification of the
 *    control.  Implementations MUST be prepared to handle arbitrary
 *    contents of the controlValue octet string, including zero bytes.  It
 *    is absent only if there is no value information that is associated
 *    with a control of its type.  When a controlValue is defined in terms
 *    of ASN.1, and BER-encoded according to Section 5.1, it also follows
 *    the extensibility rules in Section 4.
 *
 * Servers list the controlType of request controls they recognize in
 *    the 'supportedControl' attribute in the root DSE (Section 5.1 of
 *    [RFC4512]).
 *
 * Controls SHOULD NOT be combined unless the semantics of the
 *    combination has been specified.  The semantics of control
 *    combinations, if specified, are generally found in the control
 *    specification most recently published.  When a combination of
 *    controls is encountered whose semantics are invalid, not specified
 *    (or not known), the message is considered not well-formed; thus, the
 *    operation fails with protocolError.  Controls with a criticality of
 *    FALSE may be ignored in order to arrive at a valid combination.
 *    Additionally, unless order-dependent semantics are given in a
 *    specification, the order of a combination of controls in the SEQUENCE
 *    is ignored.  Where the order is to be ignored but cannot be ignored
 *    by the server, the message is considered not well-formed, and the
 *    operation fails with protocolError.  Again, controls with a
 *    criticality of FALSE may be ignored in order to arrive at a valid
 *    combination.
 *
 * This document does not specify any controls.  Controls may be
 *    specified in other documents.  Documents detailing control extensions
 *    are to provide for each control:
 *
 * - the OBJECT IDENTIFIER assigned to the control,
 *
 * - direction as to what value the sender should provide for the
 *      criticality field (note: the semantics of the criticality field are
 *      defined above should not be altered by the control's
 *      specification),
 *
 * - whether the controlValue field is present, and if so, the format of
 *      its contents,
 *
 * - the semantics of the control, and
 *
 * - optionally, semantics regarding the combination of the control with
 *      other controls.
 * @see BIND_REQUEST
 * @see BIND_RESPONSE
 * @see UNBIND_REQUEST
 * @see SEARCH_REQUEST
 * @see SEARCH_RES_ENTRY
 * @see SEARCH_RES_DONE
 * @see MODIFY_REQUEST
 * @see MODIFY_RESPONSE
 * @see ADD_REQUEST
 * @see ADD_RESPONSE
 * @see DEL_REQUEST
 * @see DEL_RESPONSE
 * @see MODIFY_DN_REQUEST
 * @see MODIFY_DN_RESPONSE
 * @see COMPARE_REQUEST
 * @see COMPARE_RESPONSE
 * @see ABANDON_REQUEST
 * @see SEARCH_RES_REF
 * @see EXTENDED_REQUEST
 * @see EXTENDED_RESPONSE
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.1.11">RFC4511 Section 4.1.11</a>
 */
enum class LDAPOperationCode(val id: Int) {
    /**
     * The function of the Bind operation is to allow authentication
     *    information to be exchanged between the client and server.  The Bind
     *    operation should be thought of as the "authenticate" operation.
     *    Operational, authentication, and security-related semantics of this
     *    operation are given in [RFC4513].
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.2">RFC4511 Section 4.2</a>
     */
    BIND_REQUEST(0),
    /**
     * BindResponse consists simply of an indication from the server of the
     *    status of the client's request for authentication.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.2.2">RFC4511 Section 4.2.2</a>
     */
    BIND_RESPONSE(1),
    /**
     * The function of the Unbind operation is to terminate an LDAP session.
     *    The Unbind operation is not the antithesis of the Bind operation as
     *    the name implies.  The naming of these operations are historical.
     *    The Unbind operation should be thought of as the "quit" operation.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.3">RFC4511 Section 4.3</a>
     */
    UNBIND_REQUEST(2),
    /**
     * The Search operation is used to request a server to return, subject
     *    to access controls and other restrictions, a set of entries matching
     *    a complex search criterion.  This can be used to read attributes from
     *    a single entry, from entries immediately subordinate to a particular
     *    entry, or from a whole subtree of entries.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5">RFC4511 Section 4.5</a>
     */
    SEARCH_REQUEST(3),
    /**
     * The results of the Search operation are returned as zero or more
     *    SearchResultEntry and/or SearchResultReference messages, followed by
     *    a single SearchResultDone message.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.2">RFC4511 Section 4.5.2</a>
     */
    SEARCH_RES_ENTRY(4),
    /**
     * The results of the Search operation are returned as zero or more
     *    SearchResultEntry and/or SearchResultReference messages, followed by
     *    a single SearchResultDone message.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.2">RFC4511 Section 4.5.2</a>
     */
    SEARCH_RES_DONE(5),
    /**
     * The Modify operation allows a client to request that a modification
     *    of an entry be performed on its behalf by a server.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.6">RFC4511 Section 4.6</a>
     */
    MODIFY_REQUEST(6),
    /**
     * Upon receipt of a Modify Request, the server attempts to perform the
     *    necessary modifications to the DIT and returns the result in a Modify
     *    Response.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.6">RFC4511 Section 4.6</a>
     */
    MODIFY_RESPONSE(7),
    /**
     * The Add operation allows a client to request the addition of an entry
     *    into the Directory.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.7">RFC4511 Section 4.7</a>
     */
    ADD_REQUEST(8),
    /**
     * Upon receipt of an Add Request, a server will attempt to add the
     *    requested entry.  The result of the Add attempt will be returned to
     *    the client in the Add Response.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.7">RFC4511 Section 4.7</a>
     */
    ADD_RESPONSE(9),
    /**
     * The Delete operation allows a client to request the removal of an
     *    entry from the Directory.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.8">RFC4511 Section 4.8</a>
     */
    DEL_REQUEST(10),
    /**
     * Upon receipt of a Delete Request, a server will attempt to perform
     *    the entry removal requested and return the result in the Delete
     *    Response.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.8">RFC4511 Section 4.8</a>
     */
    DEL_RESPONSE(11),
    /**
     * The Modify DN operation allows a client to change the Relative
     *    Distinguished Name (RDN) of an entry in the Directory and/or to move
     *    a subtree of entries to a new location in the Directory.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.9">RFC4511 Section 4.9</a>
     */
    MODIFY_DN_REQUEST(12),
    /**
     * Upon receipt of a ModifyDNRequest, a server will attempt to perform
     *    the name change and return the result in the Modify DN Response.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.9">RFC4511 Section 4.9</a>
     */
    MODIFY_DN_RESPONSE(13),
    /**
     * The Compare operation allows a client to compare an assertion value
     *    with the values of a particular attribute in a particular entry in
     *    the Directory.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.10">RFC4511 Section 4.10</a>
     */
    COMPARE_REQUEST(14),
    /**
     * Upon receipt of a Compare Request, a server will attempt to perform
     *    the requested comparison and return the result in the Compare
     *    Response.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.10">RFC4511 Section 4.10</a>
     */
    COMPARE_RESPONSE(15),
    /**
     * The function of the Abandon operation is to allow a client to request
     *    that the server abandon an uncompleted operation.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.11">RFC4511 Section 4.11</a>
     */
    ABANDON_REQUEST(16),
    /**
     * If the server was able to locate the entry referred to by the
     *    baseObject but was unable or unwilling to search one or more non-
     *    local entries, the server may return one or more
     *    SearchResultReference messages, each containing a reference to
     *    another set of servers for continuing the operation.  A server MUST
     *    NOT return any SearchResultReference messages if it has not located
     *    the baseObject and thus has not searched any entries.  In this case,
     *    it would return a SearchResultDone containing either a referral or
     *    noSuchObject result code (depending on the server's knowledge of the
     *    entry named in the baseObject).
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.5.3">RFC4511 Section 4.5.3</a>
     */
    SEARCH_RES_REF(19),
    /**
     * The Extended operation allows additional operations to be defined for
     *    services not already available in the protocol; for example, to Add
     *    operations to install transport layer security (see Section 4.14).
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.12">RFC4511 Section 4.12</a>
     */
    EXTENDED_REQUEST(23),
    /**
     * The server will respond to an ExtendedRequest with an LDAPMessage containing an
     *    ExtendedResponse.
     * @see <a href="https://datatracker.ietf.org/doc/html/rfc4511#section-4.12">RFC4511 Section 4.12</a>
     */
    EXTENDED_RESPONSE(24);

    companion object {
        /**
         * Converts an integer operation code into the appropriate enum value.
         * Returns null if an invalid code is provided.
         *
         * @param code The result code.
         * @return The corresponding operation enum, or null if not found.
         */
        fun fromId(code: Int): LDAPOperationCode? {
            return entries.find { it.id == code }
        }
    }
}