package com.theapache64.tracktor.ui.activities.userdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.theapache64.tracktor.R
import com.theapache64.tracktor.databinding.ActivityUserDetailBinding
import com.theapache64.tracktor.ui.adapters.EventDetailsAdapter
import com.theapache64.tracktor.ui.adapters.UserEventsAdapter
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.Resource
import com.theapache64.twinkill.utils.extensions.bindContentView
import dagger.android.AndroidInjection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class UserDetailActivity : BaseAppCompatActivity() {

    companion object {
        const val KEY_USER_ID = "user_id"
        fun getStartIntent(context: Context, userId: Long): Intent {
            return Intent(context, UserDetailActivity::class.java).apply {
                // data goes here
                putExtra(KEY_USER_ID, userId)
            }
        }
    }


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var viewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = bindContentView(R.layout.activity_user_detail)
        viewModel = ViewModelProvider(this, factory).get(UserDetailViewModel::class.java)


        // Watch for delete dialog
        viewModel.deleteUserDialog.observe(this, Observer {
            showDeleteUserDialog()
        })

        // Watching for finish activity
        viewModel.finishActivity.observe(this, Observer {
            onBackPressed()
        })

        viewModel.loadingView.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.lvLoadEvents.showLoading(it.data as Int)
                }
                Resource.Status.SUCCESS -> {
                    binding.lvLoadEvents.hideLoading()
                }
                Resource.Status.ERROR -> {
                    binding.lvLoadEvents.showError(it.message!!)
                }
            }
        })

        // Watching for url event
        viewModel.viewUrl.observe(this, Observer { url ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        })

        // Watching for events data
        viewModel.events.observe(this, Observer { events ->
            val adapter = UserEventsAdapter(
                events,
                { position ->
                    val event = events[position]
                    EventDetailsAdapter(event.details) { detailPosition ->
                        val detail = event.details[detailPosition]
                        viewModel.onEventDetailClicked(event, detail)
                    }
                }
            ) { eventPosition ->
                viewModel.onEventClicked(events[eventPosition])
            }

            binding.rvUserEvents.adapter = adapter
        })

        val userId = intent.getLongExtra(KEY_USER_ID, -1)
        require(userId != -1L) { "Invalid user id" }
        viewModel.init(userId)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun finishAfterTransition() {
        finish()
    }

    private fun showDeleteUserDialog() {
        MaterialDialog(this).show {
            title(R.string.user_detail_dialog_title_delete_user)
            message(R.string.user_detail_dialog_message_delete_user)
            positiveButton(R.string.action_yes) {
                viewModel.performDeleteUser()
            }
        }
    }
}
