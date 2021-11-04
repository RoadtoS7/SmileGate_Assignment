package com.smilegate.assignment.di

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class AuthManager {
    fun isValidPassword(password: String, encryptedPassword: String): Boolean {
        val cipherPassword = encryptPassword(password)
        return cipherPassword == encryptedPassword
    }

    fun encryptPassword(password: String): String {
        val plaintext: ByteArray = password.toByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)

        val key: SecretKey = keygen.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val cipherBytes: ByteArray = cipher.doFinal(plaintext)
        return String(Base64.encode(cipherBytes, Base64.DEFAULT))
    }
}
