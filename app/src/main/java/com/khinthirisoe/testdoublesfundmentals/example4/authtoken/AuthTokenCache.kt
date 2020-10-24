package com.khinthirisoe.testdoublesfundmentals.example4.authtoken

interface AuthTokenCache {
    fun cacheAuthToken(authToken: String?)
    val authToken: String?
}