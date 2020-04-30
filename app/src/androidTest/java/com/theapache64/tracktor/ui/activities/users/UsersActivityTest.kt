package com.theapache64.tracktor.ui.activities.users

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.ui.activities.userdetail.UserDetailActivity
import com.theapache64.tracktor.utils.DaggerMockDbRule
import com.theapache64.tracktor.utils.test.watchIntent
import com.theapache64.twinkill.utils.test.IdlingRule
import com.theapache64.twinkill.utils.test.monitorActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UsersActivityTest {

    @get:Rule
    val mockDbRule = DaggerMockDbRule()

    @get:Rule
    val idlingRule = IdlingRule()

    private lateinit var ac: ActivityScenario<UsersActivity>

    @Before
    fun before() {
        ac = ActivityScenario.launch(UsersActivity::class.java)
        idlingRule.dataBindingIdlingResource.monitorActivity(ac)
    }

    @After
    fun after() {
        ac.close()
        mockDbRule.db.close()
    }

    @Test
    fun givenValidUser_whenAddUserClicked_thenAddUserSuccess() {

        val validUsername = "theapache64"

        // Adding user
        clickOn(R.id.ib_add_user)
        writeTo(R.id.md_input_message, validUsername)
        clickOn(android.R.string.ok)

        // Checking if added in recyclerview
        assertDisplayedAtPosition(R.id.users_rv_users, 0, R.id.tv_item_user_name, validUsername)
    }

    @Test
    fun giveInvalidUser_whenAddUserClicked_thenError() {

        val invalidUsername = "458734857dfkjfgskdj"

        // Adding invalid user
        clickOn(R.id.ib_add_user)
        writeTo(R.id.md_input_message, invalidUsername)
        clickOn(android.R.string.ok)

        // Checking error shown
        assertDisplayed(R.string.users_error_invalid_user)
    }

    @Test
    fun givenValidUser_whenClicked_thenGoToDetailActivity() = runBlocking {

        // Adding fake user
        val fakeUser = UserEntity(
            "theapache64",
            "",
            "",
            "The Apache64"
        ).apply {
            id = 100
        }

        mockDbRule.db.userDao().insert(fakeUser)

        watchIntent {
            clickListItem(R.id.users_rv_users, 0)
            intended(
                allOf(
                    hasComponent(UserDetailActivity::class.java.name),
                    hasExtra(UserDetailActivity.KEY_USER_ID, fakeUser.id)
                )
            )
        }
    }

    @Test
    fun givenUser_whenDeleteUser_thenUsersRefreshed() = runBlocking {

        // Adding fake user
        val fakeUser = UserEntity(
            "theapache64",
            "",
            "",
            "The Apache64"
        ).apply {
            id = 100
        }

        mockDbRule.db.userDao().insert(fakeUser)
        clickListItem(R.id.users_rv_users, 0) // click
        clickOn(R.id.user_detail_ib_action_delete_user) // delete
        clickOn(R.string.action_yes) // confirm
        assertNotDisplayed(R.id.users_rv_users) // hide  users list
        assertDisplayed(R.id.users_tv_no_user) // show no user text view
    }

}