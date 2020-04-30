package com.theapache64.tracktor.ui.activities.userdetail

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.utils.DaggerMockDbRule
import com.theapache64.twinkill.utils.test.IdlingRule
import com.theapache64.twinkill.utils.test.monitorActivity
import com.winterbe.expekt.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class UserDetailActivityTest {

    @get:Rule
    val mockDbRule = DaggerMockDbRule()

    @get:Rule
    val idlingRule = IdlingRule()

    private lateinit var ac: ActivityScenario<UserDetailActivity>

    private val fakeUser = UserEntity(
        "theapache64",
        "",
        "",
        "theapache64"
    ).apply {
        id = 100
    }

    @Before
    fun setUp() = runBlocking {
        // Add fake user
        mockDbRule.db.userDao().insert(fakeUser)

        val startIntent = UserDetailActivity.getStartIntent(getApplicationContext(), fakeUser.id)
        ac = ActivityScenario.launch(startIntent)
        idlingRule.dataBindingIdlingResource.monitorActivity(ac)
    }

    @After
    fun tearDown() {
        // ac.close()
        mockDbRule.db.close()
    }


    @Test
    fun givenUser_whenActivityLaunched_NameBackButtonAndDeleteDisplayed() {
        assertDisplayed(R.id.tv_user_detail_user_name, fakeUser.name)
        assertDisplayed(R.id.user_detail_ib_action_go_back)
        assertDisplayed(R.id.user_detail_ib_action_delete_user)
    }

    @Test
    fun givenUser_whenDeletePressed_confirmationAskedAndDeletedAndClosed() {
        clickOn(R.id.user_detail_ib_action_delete_user)
        assertDisplayed(R.string.user_detail_dialog_title_delete_user)
        assertDisplayed(R.string.user_detail_dialog_message_delete_user)
        clickOn(R.string.action_yes)
        ac.state.should.equal(Lifecycle.State.DESTROYED)
    }

    @Test
    fun givenUser_whenBackButtonPressed_thenActivityClosed() {
        clickOn(R.id.user_detail_ib_action_go_back)
        ac.state.should.equal(Lifecycle.State.DESTROYED)
    }

    @Test
    fun givenUser_whenDisplayed_thenEventsDisplayed() {
        assertDisplayed(R.id.rv_user_events)
        assertRecyclerViewItemCount(R.id.rv_user_events, 30)
    }

    @Test
    fun givenUser_whenEventDetailClicked_thenIntentFired() {
        Intents.init()
        intending(hasAction(Intent.ACTION_VIEW)).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                Intent()
            )
        )
        clickListItem(R.id.rv_user_events, 0)
        intended(hasAction(Intent.ACTION_VIEW))
        Intents.release()
    }
}