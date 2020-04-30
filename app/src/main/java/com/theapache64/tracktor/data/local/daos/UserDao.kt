package com.theapache64.tracktor.data.local.daos

import androidx.room.*
import com.theapache64.tracktor.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByUserId(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users ORDER BY score DESC")
    fun getAllOrderByWatchCount(): Flow<List<UserEntity>>

    @Insert
    fun insertAll(users: List<UserEntity>)

    @Delete
    suspend fun delete(user: UserEntity): Int

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    fun deleteAll(users: List<UserEntity>): Int
}
