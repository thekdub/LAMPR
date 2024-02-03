package com.thekdub.objects;

import com.thekdub.enums.LDAPFilterCode

public class LDAPSearchFilter(
    val tag: LDAPFilterCode,
    val key: String,
    val value: String) {


    override fun toString(): String {
        return "${javaClass.name}={" +
                "tag: $tag, " +
                "key: $key, " +
                "value: $value}"
    }
}