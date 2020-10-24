package com.khinthirisoe.testdoublesfundmentals.exercise4.users

interface UsersCache {
    fun cacheUser(user: User?)
    fun getUser(userId: String?): User?
}