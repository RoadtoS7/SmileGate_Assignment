package com.smilegate.assignment.util

import android.util.Base64
import java.security.MessageDigest

object AuthManager {
    fun isValidPassword(password: String, encryptedPassword: String): Boolean {
        val cipherPassword = encryptPassword(password)
        return cipherPassword == encryptedPassword
    }

    fun encryptPassword(password: String): String {
        val message = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(message)
        return String(Base64.encode(digest, Base64.DEFAULT))
    }
}
