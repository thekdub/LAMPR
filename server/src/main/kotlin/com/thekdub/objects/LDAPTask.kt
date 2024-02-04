package com.thekdub.objects;

import com.thekdub.enums.LDAPOperationCode
import org.bouncycastle.asn1.ASN1Sequence

class LDAPTask(
    val messageID: Int,
    val operation: LDAPOperationCode,
    val data: ASN1Sequence,
    val priority: Int,
): Comparable<LDAPTask> {
    override fun compareTo(other: LDAPTask): Int = this.priority.compareTo(other.priority)
}
