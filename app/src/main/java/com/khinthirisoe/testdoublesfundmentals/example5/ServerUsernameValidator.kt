package com.khinthirisoe.testdoublesfundmentals.example5

object ServerUsernameValidator {

    @JvmStatic
    fun isValidUsername(username: String?): Boolean {
        // this sleep mimics network request that checks whether username is free, but fails due to
        // absence of network connection
        return try {
            Thread.sleep(1000)
            throw RuntimeException("no network connection")
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}