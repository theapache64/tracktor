package com.theapache64.tracktor.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.theapache64.tracktor.data.local.daos.SampleDao
import com.theapache64.tracktor.data.local.entities.SampleEntity

@Database(entities = [SampleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao
}