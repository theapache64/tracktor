package com.theapache64.tracktor.ui.activities.splash


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.theapache64.tracktor.R
import com.theapache64.tracktor.databinding.ActivitySplashBinding
import com.theapache64.tracktor.ui.activities.users.UsersActivity
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import dagger.android.AndroidInjection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

class SplashActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding = bindContentView<ActivitySplashBinding>(R.layout.activity_splash)
        val viewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)
        binding.viewModel = viewModel

        // Watching activity launch command
        viewModel.launchActivityEvent.observe(this, Observer { activityName ->

            when (activityName) {

                UsersActivity::class.simpleName -> {
                    startActivity(UsersActivity.getStartIntent(this))
                }

                else -> throw IllegalArgumentException("Undefined activity id $activityName")
            }

            finish()
        })
    }

}
