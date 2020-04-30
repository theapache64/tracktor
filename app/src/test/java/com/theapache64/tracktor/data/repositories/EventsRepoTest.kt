package com.theapache64.tracktor.data.repositories

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.theapache64.tracktor.data.remote.ApiInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class EventsRepoTest {

    private val username = "theapache64"
    private val mockApiInterface: ApiInterface = mock()


    @Test
    fun givenUser_whenLoadEvents_thenEventsLoaded() {
        val eventsRepo = EventsRepo(mock(), mockApiInterface)
        eventsRepo.getEvents(username)
        verify(mockApiInterface).getEvents(username)
    }
}