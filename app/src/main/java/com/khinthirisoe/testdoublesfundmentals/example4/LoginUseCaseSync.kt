package com.khinthirisoe.testdoublesfundmentals.example4

import com.khinthirisoe.testdoublesfundmentals.example4.authtoken.AuthTokenCache
import com.khinthirisoe.testdoublesfundmentals.example4.eventbus.EventBusPoster
import com.khinthirisoe.testdoublesfundmentals.example4.eventbus.LoggedInEvent
import com.khinthirisoe.testdoublesfundmentals.example4.networking.LoginHttpEndpointSync
import com.khinthirisoe.testdoublesfundmentals.example4.networking.NetworkErrorException

class LoginUseCaseSync(
    loginHttpEndpointSync: LoginHttpEndpointSync,
    authTokenCache: AuthTokenCache,
    eventBusPoster: EventBusPoster
) {
    enum class UseCaseResult {
        SUCCESS, FAILURE, NETWORK_ERROR
    }

    private val mLoginHttpEndpointSync: LoginHttpEndpointSync = loginHttpEndpointSync
    private val mAuthTokenCache: AuthTokenCache = authTokenCache
    private val mEventBusPoster: EventBusPoster = eventBusPoster

    fun loginSync(username: String?, password: String?): UseCaseResult {
        val endpointEndpointResult: LoginHttpEndpointSync.EndpointResult? = try {
            mLoginHttpEndpointSync.loginSync(username, password)
        } catch (e: NetworkErrorException) {
            return UseCaseResult.NETWORK_ERROR
        }

        return if (isSuccessfulEndpointResult(endpointEndpointResult)) {
            mAuthTokenCache.cacheAuthToken(endpointEndpointResult?.authToken)
            mEventBusPoster.postEvent(LoggedInEvent())
            UseCaseResult.SUCCESS
        } else {
            UseCaseResult.FAILURE
        }
    }

    private fun isSuccessfulEndpointResult(endpointResult: LoginHttpEndpointSync.EndpointResult?): Boolean {
        return endpointResult?.status == LoginHttpEndpointSync.EndpointResultStatus.SUCCESS
    }

}