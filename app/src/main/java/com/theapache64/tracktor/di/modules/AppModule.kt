package com.theapache64.tracktor.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application {
        return this.application
    }

}