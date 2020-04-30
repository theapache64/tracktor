package com.theapache64.tracktor.di.components

import com.theapache64.tracktor.App
import com.theapache64.tracktor.di.modules.*
import com.theapache64.twinkill.di.modules.PreferenceModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidSupportInjectionModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        ViewModelModule::class,
        ActivitiesBuilderModule::class,
        RepoModule::class,
        PreferenceModule::class
    ]
)
interface AppComponent {

    // inject the above given modules into this App class
    fun inject(app: App)
}