package com.smilegate.assignment

import com.smilegate.assignment.util.AuthManager
import junit.framework.TestCase

class AuthManagerTest : TestCase() {

    fun testIsValidPassword() {
        // given
        val password = "ksh123"
        val twinPassword = "ksh123"
        val encryptedPassword = AuthManager.encryptPassword(password)

        // when
        val result = AuthManager.isValidPassword(twinPassword, encryptedPassword)

        // then
        assertTrue(result)
    }
}