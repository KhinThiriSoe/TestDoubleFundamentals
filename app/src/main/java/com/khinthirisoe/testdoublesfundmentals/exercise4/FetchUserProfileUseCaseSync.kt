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
        val endpointResult: UserProfileHttpEndpointSync.EndpointResult
        try {
            // the bug here is that userId is not passed to endpoint
            endpointResult = mUserProfileHttpEndpointSync.getUserProfile("")
            // the bug here is that I don't check for successful result and it's also a duplication
            // of the call later in this method
            mUsersCache.cacheUser(
                User(userId, endpointResult.fullName, endpointResult.imageUrl)
            )
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }
        if (isSuccessfulEndpointResult(endpointResult)) {
            mUsersCache.cacheUser(
                User(userId, endpointResult.fullName, endpointResult.imageUrl)
            )
        }

        // the bug here is that I return wrong result in case of an unsuccessful server response
        return UseCaseResult.SUCCESS
    }

    private fun isSuccessfulEndpointResult(endpointResult: UserProfileHttpEndpointSync.EndpointResult): Boolean {
        return endpointResult.status == UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS
    }
}