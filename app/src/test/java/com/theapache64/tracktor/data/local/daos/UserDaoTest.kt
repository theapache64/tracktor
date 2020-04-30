package com.theapache64.tracktor.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.theapache64.tracktor.data.local.AppDatabase
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.winterbe.expekt.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UserDaoTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao


    @Before
    fun before() {
        this.database =
            Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        this.userDao = database.userDao()
    }

    @After
    fun after() {
        this.database.close()
    }

    @Test
    fun givenUser_whenInsertUser_thenAddedToDbSuccess() = runBlockingTest {

        // given : Creating input data with whenInsertUser
        val user = UserEntity(
            "theapache64",
            "https://avatars1.githubusercontent.com/u/9678279?s=250&u=05c31bfdede43ddfb9b15e5b07f00cca7a3bb42d&v=4",
            "user",
            "theapache64"
        )

        // when : Calling subject -> givenUser
        userDao.insert(user)

        // then : Asserting the condition -> thenAddedToDbSuccess
        val addedUser = database.userDao().getUserByUsername(user.username)
        addedUser!!.name.should.equal(user.username)
    }

    @Test
    fun givenUser_whenGetAll_thenSortInCorrectOrder() = runBlockingTest {

        // given : Creating input data with whenGetAll
        val mostInterestedUsername = "most-interested-user"
        val normalUsername = "normal-user"
        val leastInterestedUsername = "least-interested-user"

        val users = listOf(
            UserEntity(mostInterestedUsername, "", "", "").apply {
                id = 1
                score = 3
            },
            UserEntity(leastInterestedUsername, "", "", "").apply {
                id = 3
                score = 1
            },
            UserEntity(normalUsername, "", "", "").apply {
                id = 2
                score = 2
            }
        )


        userDao.insertAll(users)
        val addedUsers = userDao.getAllOrderByWatchCount().first()
        addedUsers.size.should.equal(users.size)

        // Should be in same order
        addedUsers.map { it.username }.should.equal(
            listOf(
                mostInterestedUsername,
                normalUsername,
                leastInterestedUsername
            )
        )
    }

    @Test
    fun givenNewUsername_whenAddDelete_thenDeleteFromDb() = runBlockingTest {

        // given : Creating input data with whenAddDelete
        val username = "theapache64"

        // Adding first
        userDao.insert(UserEntity(username, "", "", ""))

        // Check if added
        val addedUser = userDao.getUserByUsername(username)
        addedUser.should.not.`null`

        // Delete from db by username
        userDao.delete(addedUser!!)

        // Check if exists
        userDao.getUserByUsername(username).should.be.`null`
    }

}