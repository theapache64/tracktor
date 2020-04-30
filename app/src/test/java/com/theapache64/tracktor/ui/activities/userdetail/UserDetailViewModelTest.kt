package com.theapache64.tracktor.ui.activities.userdetail

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.theapache64.tracktor.models.UserEvent
import com.theapache64.twinkill.test.getOrAwaitValue
import com.winterbe.expekt.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4::class)
class UserDetailViewModelTest {


    @Test
    fun givenUserEvent_whenOnEventClicked_thenUrlLiveDataChanged() {

        // given : Creating input data with whenOnEventClicked
        val viewModel = UserDetailViewModel(mock(), mock())
        val fakeUserEvent: UserEvent = mock()
        whenever(fakeUserEvent.url).thenReturn("http://github.com/fakeuser/fakerepo")

        viewModel.onEventClicked(fakeUserEvent)
        viewModel.viewUrl.getOrAwaitValue().should.equal(fakeUserEvent.url)
    }
}