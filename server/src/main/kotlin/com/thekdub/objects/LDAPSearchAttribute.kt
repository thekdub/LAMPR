package com.thekdub.objects

class LDAPSearchAttribute(
    val value: String
) {
    override fun toString(): String {
        return "${javaClass.name}={" +
                "value: $value}"
    }
}