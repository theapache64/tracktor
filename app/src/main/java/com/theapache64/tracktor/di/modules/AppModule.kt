package com.theapache64.tracktor.di.modules

import android.app.Application

import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        AndroidSupportInjectionModule::class,
        NetworkModule::class,

        DatabaseModule::class,
        ViewModelModule::class,
        ActivitiesBuilderModule::class
    ]
)
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application {
        return this.application
    }

}