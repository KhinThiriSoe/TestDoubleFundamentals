package com.khinthirisoe.testdoublesfundmentals.exercise4.networking

import com.khinthirisoe.testdoublesfundmentals.example4.networking.NetworkErrorException
import kotlin.Throws

interface UserProfileHttpEndpointSync {
    /**
     * Get user's profile from the server
     * @return the aggregated result
     * @throws NetworkErrorException if operation failed due to network error
     */
    @Throws(NetworkErrorException::class)
    fun getUserProfile(userId: String?): EndpointResult?
    enum class EndpointResultStatus {
        SUCCESS, AUTH_ERROR, SERVER_ERROR, GENERAL_ERROR
    }

    class EndpointResult(
        val status: EndpointResultStatus,
        val userId: String,
        val fullName: String,
        val imageUrl: String
    )
}