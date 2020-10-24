package com.khinthirisoe.testdoublesfundmentals.example5

import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserInputValidatorTest {

    lateinit var SUT: UserInputValidator

    @Before
    @Throws(Exception::class)
    fun setup() {
        SUT = UserInputValidator()
    }

    @Test
    @Throws(Exception::class)
    fun isValidFullName_validFullName_trueReturned() {
        val result = SUT.isValidFullName("validFullName")
        Assert.assertThat(result, CoreMatchers.`is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun isValidFullName_invalidFullName_falseReturned() {
        val result = SUT.isValidFullName("")
        Assert.assertThat(result, CoreMatchers.`is`(false))
    }

    @Test
    @Throws(Exception::class)
    fun isValidUsername_validUsername_trueReturned() {
        val result = SUT.isValidUsername("validUsername")
        Assert.assertThat(result, CoreMatchers.`is`(true))
    }

    @Test
    @Throws(Exception::class)
    fun isValidUsername_invalidUsername_falseReturned() {
        val result = SUT.isValidUsername("")
        Assert.assertThat(result, CoreMatchers.`is`(false))
    }
}