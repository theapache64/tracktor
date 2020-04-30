package com.theapache64.tracktor.di.modules


import com.theapache64.tracktor.ui.activities.splash.SplashActivity
import com.theapache64.tracktor.ui.activities.userdetail.UserDetailActivity
import com.theapache64.tracktor.ui.activities.users.UsersActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * To hold activities to support AndroidInjection call from dagger.
 */
@FlowPreview
@ExperimentalCoroutinesApi
@Module
abstract class ActivitiesBuilderModule {


    @ContributesAndroidInjector
    abstract fun getSplashActivity(): SplashActivity


    @ContributesAndroidInjector
    abstract fun getUsersActivity(): UsersActivity

    @ContributesAndroidInjector
    abstract fun getUserDetailActivity(): UserDetailActivity

}