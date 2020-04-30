package com.theapache64.tracktor.ui.activities.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.theapache64.tracktor.data.remote.user.User
import com.theapache64.tracktor.data.repositories.PrefRepo
import com.theapache64.tracktor.data.repositories.UserRepo
import com.theapache64.tracktor.utils.test.observeForTesting
import com.theapache64.twinkill.network.utils.Resource
import com.theapache64.twinkill.test.MainCoroutineRule
import com.theapache64.twinkill.test.getOrAwaitValue
import com.theapache64.twinkill.utils.test.IdlingRule
import com.winterbe.expekt.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4::class)
class UsersViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val idleRule = IdlingRule()

    @Test
    fun givenUserActivity_whenUserAddClicked_thenUserAddClickLiveDataChanges() {

        val fakeUserRepo: UserRepo = mock()
        whenever(fakeUserRepo.getUsers()).thenReturn(flowOf(listOf())) // empty db list
        val userViewModel = UsersViewModel(fakeUserRepo, mock())
        userViewModel.addUserClick.value.should.`null`
        userViewModel.onAddUserClicked()
        userViewModel.addUserClick.getOrAwaitValue().should.`true`
    }

    @Test
    fun givenNewUsername_whenAddInvalidUser_thenError() = coroutineRule.runBlockingTest {
        val invalidUsername = "SomeInvalidUsername^&^&*%^&%$^$"

        val fakeUserRepo: UserRepo = mock()
        whenever(fakeUserRepo.getUsers()).thenReturn(flowOf(listOf())) // empty db list
        whenever(fakeUserRepo.getUserLocal(invalidUsername)).thenReturn(null) //  user not available in local db
        whenever(fakeUserRepo.getUserRemote(invalidUsername)).thenReturn(flow {
            emit(Resource.loading<User>())
            delay(2000) // fake delay
            emit(Resource.error("Invalid user", UsersViewModel.STATUS_INVALID_USER))
        }) // empty list

        val userViewModel = UsersViewModel(fakeUserRepo, mock())

        // when : Calling subject -> givenNewUsername
        userViewModel.addUser(invalidUsername)
        userViewModel.onAddUserChannel.value.should.equal(invalidUsername)

        userViewModel.addUserResponse.observeForTesting {
            val firstResponse = userViewModel.addUserResponse.value!!
            firstResponse.status.should.equal(Resource.Status.LOADING)
            delay(2000)
            val secondResponse = userViewModel.addUserResponse.value!!
            secondResponse.status.should.equal(Resource.Status.ERROR)
            secondResponse.statusCode.should.equal(UsersViewModel.STATUS_INVALID_USER)
        }
    }


    @Test
    fun givenNewUsername_whenAddValidUser_thenSuccess() = coroutineRule.runBlockingTest {
        val validUsername = "theapache64"

        val fakeUserRepo: UserRepo = mock()
        whenever(fakeUserRepo.getUsers()).thenReturn(flowOf(listOf())) // empty db list
        whenever(fakeUserRepo.saveUser(any())).thenReturn(Unit)
        whenever(fakeUserRepo.getUserLocal(validUsername)).thenReturn(null) //  user not available in local db
        whenever(fakeUserRepo.getUserRemote(validUsername)).thenReturn(flow {
            emit(Resource.loading<User>())
            delay(2000) // fake delay
            val newFakeUser = User(
                validUsername,
                "",
                "",
                ""
            )
            emit(Resource.success(newFakeUser, -1))
        }) // empty list

        val userViewModel = UsersViewModel(fakeUserRepo, mock())

        // when : Calling subject -> givenNewUsername
        userViewModel.addUser(validUsername)
        userViewModel.onAddUserChannel.value.should.equal(validUsername)

        userViewModel.addUserResponse.observeForTesting {

            val firstResponse = userViewModel.addUserResponse.value!!
            firstResponse.status.should.equal(Resource.Status.LOADING)
            delay(2000)
            val secondResponse = userViewModel.addUserResponse.value!!
            secondResponse.status.should.equal(Resource.Status.SUCCESS)

            verify(fakeUserRepo).saveUser(any())
        }
    }

    @Test
    fun givenNightMode_whenToggleNightModeClicked_thenNightModeChanged() {

        val fakePrefRepo: PrefRepo = mock()
        whenever(fakePrefRepo.isNightModeEnabled()).thenReturn(true)

        val userViewModel = UsersViewModel(mock(), fakePrefRepo)

        // Default
        userViewModel.nightMode.value.should.`null`

        // Toggle
        userViewModel.onToggleNightModeClicked()

        // Checking
        verify(fakePrefRepo).isNightModeEnabled()

        // Change
        userViewModel.nightMode.value.should.be.`false`

        // Save
        verify(fakePrefRepo).setNightModeEnabled(false)
    }


}