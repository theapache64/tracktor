package com.theapache64.tracktor.di.modules


import com.theapache64.tracktor.ui.activities.users.UsersActivity
import com.theapache64.tracktor.ui.activities.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * To hold activities to support AndroidInjection call from dagger.
 */
@Module
abstract class ActivitiesBuilderModule {


    @ContributesAndroidInjector
    abstract fun getSplashActivity(): SplashActivity


    @ContributesAndroidInjector
    abstract fun getUsersActivity(): UsersActivity
}