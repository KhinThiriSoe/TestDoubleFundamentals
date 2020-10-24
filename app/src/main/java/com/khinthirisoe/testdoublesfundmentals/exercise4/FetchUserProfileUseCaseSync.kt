package com.khinthirisoe.testdoublesfundmentals.exercise4

import com.khinthirisoe.testdoublesfundmentals.example4.networking.NetworkErrorException
import com.khinthirisoe.testdoublesfundmentals.exercise4.networking.UserProfileHttpEndpointSync
import com.khinthirisoe.testdoublesfundmentals.exercise4.users.User
import com.khinthirisoe.testdoublesfundmentals.exercise4.users.UsersCache

class FetchUserProfileUseCaseSync(
    private val mUserProfileHttpEndpointSync: UserProfileHttpEndpointSync,
    private val mUsersCache: UsersCache
) {
    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    fun fetchUserProfileSync(userId: String?): UseCaseResult {
        val endpointResult: UserProfileHttpEndpointSync.EndpointResult?
        try {
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile(userId)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return if (isSuccessfulEndpointResult(endpointResult!!)) {
            mUsersCache.cacheUser(
                userId?.let { User(it, endpointResult.fullName, endpointResult.imageUrl) }
            )
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: UserProfileHttpEndpointSync.EndpointResult): Boolean {
        return endpointResult.status == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}