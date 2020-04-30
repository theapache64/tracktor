package com.theapache64.tracktor.di.modules

import android.content.Context
import androidx.room.Room
import com.theapache64.tracktor.data.local.AppDatabase
import com.theapache64.tracktor.utils.test.OpenForTesting
import com.theapache64.twinkill.di.modules.ContextModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@OpenForTesting
@Module(includes = [ContextModule::class])
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "com.theapache64.tracktor_db")
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()
}