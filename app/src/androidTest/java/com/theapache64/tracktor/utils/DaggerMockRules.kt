package com.theapache64.tracktor.utils

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.theapache64.tracktor.App
import com.theapache64.tracktor.data.local.AppDatabase
import com.theapache64.tracktor.di.components.AppComponent
import com.theapache64.tracktor.di.components.DaggerAppComponent
import com.theapache64.tracktor.di.modules.DatabaseModule
import com.theapache64.twinkill.di.modules.ContextModule
import com.theapache64.twinkill.network.di.modules.BaseNetworkModule
import it.cosenonjaviste.daggermock.DaggerMockRule


class DaggerMockDbRule : DaggerMockRule<AppComponent>(AppComponent::class.java, DatabaseModule()) {

    var db: AppDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).build()

    private val app =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App

    init {
        customizeBuilder<DaggerAppComponent.Builder> {
            it.baseNetworkModule(BaseNetworkModule(App.BASE_URL))
                .contextModule(ContextModule(app))
        }

        set {
            it.inject(app)
        }

        provides(AppDatabase::class.java, db)
    }
}


