package com.theapache64.tracktor.ui.activities.splash


import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.theapache64.tracktor.R
import com.theapache64.tracktor.databinding.ActivitySplashBinding
import com.theapache64.tracktor.ui.activities.users.UsersActivity
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashActivity : BaseAppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding = bindContentView<ActivitySplashBinding>(R.layout.activity_splash)
        val viewModel = ViewModelProvider(this, factory).get(SplashViewModel::class.java)
        binding.viewModel = viewModel

        // Watching activity launch command
        viewModel.getLaunchActivityEvent().observe(this, Observer { activityName ->

            when (activityName) {
                UsersActivity::class.simpleName -> {
                    startActivity(UsersActivity.getStartIntent(this))
                }

                else -> throw IllegalArgumentException("Undefined activity id $activityName")
            }

            finish()

        })

        // Starting splash timer
        Handler().postDelayed({
            viewModel.goToNextScreen()
        }, SPLASH_DURATION)

    }


    companion object {
        private const val SPLASH_DURATION = 1000L
    }

}
