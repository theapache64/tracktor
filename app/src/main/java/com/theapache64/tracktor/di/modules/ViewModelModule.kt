package com.theapache64.tracktor.di.modules

import androidx.lifecycle.ViewModel
import com.theapache64.tracktor.ui.activities.main.UsersViewModel
import com.theapache64.tracktor.ui.activities.splash.SplashViewModel
import com.theapache64.twinkill.di.modules.BaseViewModelModule
import com.theapache64.twinkill.utils.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module(includes = [BaseViewModelModule::class])
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(UsersViewModel::class)
    abstract fun bindUsersViewModel(viewModel: UsersViewModel): ViewModel
}