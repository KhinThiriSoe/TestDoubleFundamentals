package com.khinthirisoe.testdoublesfundmentals.example5

class UserInputValidator {
    fun isValidFullName(fullName: String?): Boolean {
        return FullNameValidator.isValidFullName(fullName!!)
    }

    fun isValidUsername(username: String?): Boolean {
        return ServerUsernameValidator.isValidUsername(username)
    }
}