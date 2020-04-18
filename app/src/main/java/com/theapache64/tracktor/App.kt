package com.theapache64.tracktor


import android.app.Application
import com.theapache64.tracktor.di.components.DaggerAppComponent
import com.theapache64.twinkill.TwinKill
import com.theapache64.twinkill.di.modules.ContextModule
import com.theapache64.twinkill.googlesans.GoogleSans
import com.theapache64.twinkill.network.di.modules.BaseNetworkModule
import com.theapache64.twinkill.network.utils.retrofit.adapters.resourcedataadapter.ResourceCallAdapter
import com.theapache64.twinkill.network.utils.retrofit.adapters.resourcedataadapter.ResourceCallAdapterFactory
import com.theapache64.twinkill.network.utils.retrofit.interceptors.AuthorizationInterceptor
import com.theapache64.twinkill.network.utils.retrofit.interceptors.CurlInterceptor
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    override fun onCreate() {
        super.onCreate()

        // Dagger
        DaggerAppComponent.builder()
            .contextModule(ContextModule(this))
            .baseNetworkModule(BaseNetworkModule(BASE_URL))
            .build()
            .inject(this)

        // TwinKill
        TwinKill.init(
            TwinKill
                .builder()

                .addCallAdapter(ResourceCallAdapterFactory(true))
                .addOkHttpInterceptor(CurlInterceptor())


                .setDefaultFont(GoogleSans.Regular)
                .build()
        )

    }

}
