package com.theapache64.tracktor.ui.activities.users


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.databinding.ActivityUsersBinding
import com.theapache64.tracktor.ui.activities.userdetail.UserDetailActivity
import com.theapache64.tracktor.ui.adapters.UsersAdapter
import com.theapache64.tracktor.utils.NightModeUtils
import com.theapache64.twinkill.network.utils.Resource
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import com.theapache64.twinkill.utils.extensions.snackBar
import dagger.android.AndroidInjection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class UsersActivity : BaseAppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, UsersActivity::class.java).apply {
                // add data here
            }
        }
    }

    private lateinit var binding: ActivityUsersBinding

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        this.binding = bindContentView(R.layout.activity_users)

        this.viewModel = ViewModelProvider(this, factory).get(UsersViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.addUserClick.observe(this, Observer {
            showAddUserDialog()
        })

        // Snack bar messages
        viewModel.getSnackBarMessage().observe(this, Observer { msg ->
            if (msg is String) {
                binding.usersRvUsers.snackBar(msg)
            } else {
                binding.usersRvUsers.snackBar(msg as Int)
            }
        })


        // Watching add new user response
        viewModel.addUserResponse.observe(this, Observer {

            when (it.status) {
                Resource.Status.LOADING -> {
                    binding.lvAddUser.showLoading(R.string.users_finding_user)
                }
                Resource.Status.SUCCESS -> {
                    binding.lvAddUser.hideLoading()
                }
                Resource.Status.ERROR -> {
                    binding.lvAddUser.hideLoading()
                }
            }
        })

        // Watching for night mode
        viewModel.nightMode.observe(this, Observer { isNightModeEnabled ->
            NightModeUtils.setNightModeEnabled(isNightModeEnabled)
        })

        // Watching all users
        viewModel.users.observe(this, Observer { users ->

            val usersAdapter = UsersAdapter(users) { position, tvUserName ->
                onUserClicked(users[position], tvUserName)
            }

            binding.usersRvUsers.adapter = usersAdapter
        })
    }

    private fun onUserClicked(userEntity: UserEntity, textView: View) {
        val animOption = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            textView,
            "user_name"
        )

        startActivity(UserDetailActivity.getStartIntent(this, userEntity.id), animOption.toBundle())
    }

    private fun showAddUserDialog() {
        MaterialDialog(this).show {
            title(res = R.string.users_dialog_title_add_user)
            message(res = R.string.users_dialog_message_add_user)
            input(hintRes = R.string.users_hint_username) { _: MaterialDialog, username: CharSequence ->
                viewModel.addUser(username.toString())
            }
        }
    }
}
