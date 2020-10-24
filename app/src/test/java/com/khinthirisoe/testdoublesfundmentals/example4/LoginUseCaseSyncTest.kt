package com.khinthirisoe.testdoublesfundmentals.example4

import com.khinthirisoe.testdoublesfundmentals.example4.authtoken.AuthTokenCache
import com.khinthirisoe.testdoublesfundmentals.example4.eventbus.EventBusPoster
import com.khinthirisoe.testdoublesfundmentals.example4.eventbus.LoggedInEvent
import com.khinthirisoe.testdoublesfundmentals.example4.networking.LoginHttpEndpointSync
import com.khinthirisoe.testdoublesfundmentals.example4.networking.NetworkErrorException
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class LoginUseCaseSyncTest {

    companion object {
        val USERNAME = "username"
        val PASSWORD = "password"
        val AUTH_TOKEN = "authToken"
        val NON_INITIALIZED_AUTH_TOKEN = "noAuthToken"
    }

    lateinit var SUT : LoginUseCaseSync
    lateinit var loginHttpEndpointSyncTd: LoginHttpEndpointSyncTd
    lateinit var authTokenCacheTd: AuthTokenCacheTd
    lateinit var postEventBusPosterTd: EventBusPosterTd

    @Before
    @Throws(Exception::class)
    fun setup() {
        loginHttpEndpointSyncTd = LoginHttpEndpointSyncTd()
        authTokenCacheTd = AuthTokenCacheTd()
        postEventBusPosterTd = EventBusPosterTd()
        SUT = LoginUseCaseSync(loginHttpEndpointSyncTd, authTokenCacheTd, postEventBusPosterTd)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(loginHttpEndpointSyncTd.mUsername, `is`(USERNAME))
        assertThat(loginHttpEndpointSyncTd.mPassword, `is`(PASSWORD))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_authTokenCached() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(authTokenCacheTd.mAuthToken, `is`(AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_authTokenNotCached() {
        loginHttpEndpointSyncTd.mIsGeneralError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(authTokenCacheTd.mAuthToken, `is`(NON_INITIALIZED_AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_authTokenNotCached() {
        loginHttpEndpointSyncTd.mIsAuthError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(authTokenCacheTd.mAuthToken, `is`(NON_INITIALIZED_AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_authTokenNotCached() {
        loginHttpEndpointSyncTd.mIsServerError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(authTokenCacheTd.mAuthToken, `is`(NON_INITIALIZED_AUTH_TOKEN))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_loggedInEventPosted() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(postEventBusPosterTd.mEvent, `is`(instanceOf(LoggedInEvent::class.java)))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_noInteractionWithLoggedInEventPosted() {
        loginHttpEndpointSyncTd.mIsGeneralError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(postEventBusPosterTd.mInteractionsCount, `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_noInteractionWithLoggedInEventPosted() {
        loginHttpEndpointSyncTd.mIsAuthError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(postEventBusPosterTd.mInteractionsCount, `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_noInteractionWithLoggedInEventPosted() {
        loginHttpEndpointSyncTd.mIsServerError = true
        SUT.loginSync(USERNAME, PASSWORD)
        assertThat(postEventBusPosterTd.mInteractionsCount, `is`(0))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_successReturned() {
        val userResult = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(userResult, `is`(LoginUseCaseSync.UseCaseResult.SUCCESS))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_failureReturned() {
        loginHttpEndpointSyncTd.mIsGeneralError = true
        val userResult = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(userResult,`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_failureReturned() {
        loginHttpEndpointSyncTd.mIsAuthError = true
        val userResult = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(userResult,`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_failureReturned() {
        loginHttpEndpointSyncTd.mIsServerError = true
        val userResult = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(userResult,`is`(LoginUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_networkError_networkErrorReturned() {
        loginHttpEndpointSyncTd.mIsNetworkError = true
        val userResult = SUT.loginSync(USERNAME, PASSWORD)
        assertThat(userResult,`is`(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR))
    }


    // ---------------------------------------------------------------------------------------------
    // Helper classes

    class LoginHttpEndpointSyncTd : LoginHttpEndpointSync {

        var mUsername: String? = ""
        var mPassword: String? = ""
        var mIsGeneralError = false
        var mIsAuthError = false
        var mIsServerError = false
        var mIsNetworkError = false

        override fun loginSync(username: String?, password: String?): LoginHttpEndpointSync.EndpointResult {
            mUsername = username
            mPassword = password
            return when {
                mIsGeneralError -> {
                    LoginHttpEndpointSync.EndpointResult(
                        LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                        ""
                    )
                }
                mIsAuthError -> {
                    LoginHttpEndpointSync.EndpointResult(
                        LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                        ""
                    )
                }
                mIsServerError -> {
                    LoginHttpEndpointSync.EndpointResult(
                        LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                        ""
                    )
                }
                mIsNetworkError -> { throw NetworkErrorException() }
                else -> { LoginHttpEndpointSync.EndpointResult(
                    LoginHttpEndpointSync.EndpointResultStatus.SUCCESS,
                    AUTH_TOKEN
                ) }
            }
        }
    }

    class AuthTokenCacheTd : AuthTokenCache {

        var mAuthToken = NON_INITIALIZED_AUTH_TOKEN

        override fun cacheAuthToken(authToken: String?) {
            if (authToken != null) {
                mAuthToken = authToken
            }
        }

        override val authToken: String?
            get() = mAuthToken

    }

    class EventBusPosterTd : EventBusPoster {

        var mEvent: Any? = null
        var mInteractionsCount = 0
        override fun postEvent(event: Any?) {
            mInteractionsCount++
            mEvent = event
        }
    }
}