package com.khinthirisoe.testdoublesfundmentals.example5

object FullNameValidator {

    @JvmStatic
    fun isValidFullName(fullName: String): Boolean {
        // trivially simple task
        return !fullName.isEmpty()
    }
}