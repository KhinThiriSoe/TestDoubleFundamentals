package com.khinthirisoe.testdoublesfundmentals.exercise4

import com.khinthirisoe.testdoublesfundmentals.example4.networking.NetworkErrorException
import com.khinthirisoe.testdoublesfundmentals.exercise4.networking.UserProfileHttpEndpointSync
import com.khinthirisoe.testdoublesfundmentals.exercise4.users.User
import com.khinthirisoe.testdoublesfundmentals.exercise4.users.UsersCache
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import kotlin.collections.ArrayList

class FetchUserProfileUseCaseSyncTest {

    companion object {
        val USER_ID = "user_id"
        val FULLNAME = "fullname"
        val IMAGE_URL = "image_url"
    }

    lateinit var SUT : FetchUserProfileUseCaseSync
    lateinit var userProfileHttpEndpointSyncTd: UserProfileHttpEndpointSyncTd
    lateinit var usersCacheTd: UsersCacheTd

    @Before
    fun setUp() {
        userProfileHttpEndpointSyncTd = UserProfileHttpEndpointSyncTd()
        usersCacheTd = UsersCacheTd()
        SUT = FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd)
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_success_UserIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USER_ID)
        assertThat(userProfileHttpEndpointSyncTd.mUserId, `is`(USER_ID))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_success_userCached() {
        SUT.fetchUserProfileSync(USER_ID)
        val cacheUser = usersCacheTd.getUser(USER_ID)
        assertThat(cacheUser?.userId, `is`(USER_ID))
        assertThat(cacheUser?.fullName, `is`(FULLNAME))
        assertThat(cacheUser?.imageUrl, `is`(IMAGE_URL))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_generalError_userNotCached() {
        userProfileHttpEndpointSyncTd.mIsGeneralError = true
        SUT.fetchUserProfileSync(USER_ID)
        assertThat(usersCacheTd.mUsers, `is`(emptyList()))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_authError_CacheNotCached() {
        userProfileHttpEndpointSyncTd.mIsAuthError = true
        SUT.fetchUserProfileSync(USER_ID)
        assertThat(usersCacheTd.mUsers, `is`(emptyList()))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_serverError_cacheNotCached() {
        userProfileHttpEndpointSyncTd.mIsServerError = true
        SUT.fetchUserProfileSync(USER_ID)
        assertThat(usersCacheTd.mUsers, `is`(emptyList()))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_success_successReturned() {
        val userResult = SUT.fetchUserProfileSync(USER_ID)
        assertThat(userResult, `is`(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_generalError_failureReturned() {
        userProfileHttpEndpointSyncTd.mIsGeneralError = true
        val userResult = SUT.fetchUserProfileSync(USER_ID)
        assertThat(userResult,`is`(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_authError_failureReturned() {
        userProfileHttpEndpointSyncTd.mIsAuthError = true
        val userResult = SUT.fetchUserProfileSync(USER_ID)
        assertThat(userResult,`is`(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_serverError_failureReturned() {
        userProfileHttpEndpointSyncTd.mIsServerError = true
        val userResult = SUT.fetchUserProfileSync(USER_ID)
        assertThat(userResult,`is`(FetchUserProfileUseCaseSync.UseCaseResult.FAILURE))
    }

    @Test
    @Throws(Exception::class)
    fun fetchUserProfileSync_networkError_networkErrorReturned() {
        userProfileHttpEndpointSyncTd.mIsNetworkError = true
        val userResult = SUT.fetchUserProfileSync(USER_ID)
        assertThat(userResult,`is`(FetchUserProfileUseCaseSync.UseCaseResult.NETWORK_ERROR))
    }


    // ---------------------------------------------------------------------------------------------
    // Helper classes

    class UserProfileHttpEndpointSyncTd: UserProfileHttpEndpointSync {

        var mUserId: String? = ""

        var mIsGeneralError = false
        var mIsAuthError = false
        var mIsServerError = false
        var mIsNetworkError = false


        override fun getUserProfile(userId: String?): UserProfileHttpEndpointSync.EndpointResult? {
            mUserId = userId
            return when {
                mIsGeneralError -> {
                    UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR,
                        "",
                        "",
                        ""
                    )
                }
                mIsAuthError -> {
                    UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.AUTH_ERROR,
                        "",
                        "",
                        ""
                    )
                }
                mIsServerError -> {
                    UserProfileHttpEndpointSync.EndpointResult(
                        UserProfileHttpEndpointSync.EndpointResultStatus.SERVER_ERROR,
                        "",
                        "",
                        ""
                    )
                }
                mIsNetworkError -> { throw NetworkErrorException() }
                else -> {
                    userId?.let {
                        UserProfileHttpEndpointSync.EndpointResult(
                            UserProfileHttpEndpointSync.EndpointResultStatus.SUCCESS,
                            it,
                            FULLNAME,
                            IMAGE_URL
                        )
                    }
                }
            }
        }

    }

    class UsersCacheTd: UsersCache {

        val mUsers: ArrayList<User> = ArrayList(1)

        override fun cacheUser(user: User?) {
            val existingUser = getUser(user?.userId)
            if (existingUser != null) {
                mUsers.remove(existingUser)
            }
            if (user != null) {
                mUsers.add(user)
            }
        }

        override fun getUser(userId: String?): User? {
            for (user in mUsers) {
                if (user.userId == userId) {
                    return user
                }
            }
            return null
        }

    }

}