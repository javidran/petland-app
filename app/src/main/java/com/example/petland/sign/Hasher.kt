package com.example.petland.sign

import java.security.MessageDigest


class Hasher {
    companion object {
        fun hash(password : String): String {
            val bytes = password.toByteArray()
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}