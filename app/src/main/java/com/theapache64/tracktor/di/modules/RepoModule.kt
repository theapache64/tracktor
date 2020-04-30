package com.theapache64.tracktor.di.modules

import com.theapache64.tracktor.data.local.daos.UserDao
import com.theapache64.tracktor.data.remote.ApiInterface
import com.theapache64.tracktor.data.repositories.UserRepo
import com.theapache64.tracktor.utils.test.OpenForTesting
import dagger.Module
import dagger.Provides

@OpenForTesting
@Module
class RepoModule {

    @Provides
    fun provideUserRepo(
        apiInterface: ApiInterface,
        userDao: UserDao
    ): UserRepo {
        return UserRepo(apiInterface, userDao)
    }
}