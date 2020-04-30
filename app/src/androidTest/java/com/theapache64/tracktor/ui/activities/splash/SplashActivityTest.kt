package com.theapache64.tracktor.ui.activities.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.theapache64.tracktor.R
import com.theapache64.tracktor.ui.activities.users.UsersActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


@FlowPreview
@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {


    @Test
    fun goToFeedAfterDelay() = runBlocking {
        // given : Creating input data with when
        Intents.init()
        val sc = ActivityScenario.launch(SplashActivity::class.java)

        // Both logo and version number displayed
        assertDisplayed(R.id.iv_logo)
        assertDisplayed(R.id.tv_version_name)

        // Checking if moved to feed
        sleep(SplashViewModel.SPLASH_DURATION)
        intended(hasComponent(UsersActivity::class.java.name))
        Intents.release()
        sc.close()
    }
}
