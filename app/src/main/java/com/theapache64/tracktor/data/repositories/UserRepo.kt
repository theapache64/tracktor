package com.theapache64.tracktor.data.repositories

import com.theapache64.tracktor.data.local.daos.UserDao
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.data.remote.ApiInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val apiInterface: ApiInterface,
    private val userDao: UserDao
) {

    fun getUserRemote(username: String) = apiInterface.getUser(username)

    suspend fun saveUser(user: UserEntity) = userDao.insert(user)

    fun getUsers() = userDao.getAllOrderByWatchCount()

    suspend fun getUserLocal(username: String): UserEntity? = userDao.getUserByUsername(username)
    fun getUserLocal(userId: Long): Flow<UserEntity?> = userDao.getUserByUserId(userId)


    suspend fun deleteUser(userEntity: UserEntity) = userDao.delete(userEntity)
    suspend fun incrementUserScore(userId: Long) {
        val user = userDao.getUserByUserId(userId).first()
        if (user != null) {
            println("Updating user score")
            user.score++
            userDao.update(user)
        } else {
            println("User null")
        }
    }
}