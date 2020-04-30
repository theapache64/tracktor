package com.theapache64.tracktor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theapache64.tracktor.data.local.daos.UserDao
import com.theapache64.tracktor.data.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}