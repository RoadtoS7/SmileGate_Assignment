package com.smilegate.assignment.di

import junit.framework.TestCase

class AuthManagerTest : TestCase() {

    fun testIsValidPassword() {
        // given
        val authManager = AuthManager()
        val password = "ksh123"
        val twinPassword = "ksh123"
        val encryptedPassword = authManager.encryptPassword(password)

        // when
        val result = authManager.isValidPassword(twinPassword, encryptedPassword)

        // then
        assertTrue(result)
    }
}