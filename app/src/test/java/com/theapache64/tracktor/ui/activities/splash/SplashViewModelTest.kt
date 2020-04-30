package com.theapache64.tracktor.ui.activities.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.theapache64.tracktor.ui.activities.users.UsersActivity
import com.theapache64.twinkill.test.MainCoroutineRule
import com.theapache64.twinkill.test.getOrAwaitValue
import com.winterbe.expekt.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = MainCoroutineRule()

    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun init() {
        splashViewModel = SplashViewModel()
    }

    @Test
    fun `After splash, should go to users list`() {
        // when : Calling subject -> navigation
        val actualResult = splashViewModel.launchActivityEvent.getOrAwaitValue {
            coroutinesRule.advanceTimeBy(SplashViewModel.SPLASH_DURATION)
        }
        actualResult.should.equal(UsersActivity::class.simpleName)
    }

}