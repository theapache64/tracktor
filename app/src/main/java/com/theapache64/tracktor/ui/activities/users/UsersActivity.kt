package com.theapache64.tracktor.ui.activities.users


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.theapache64.tracktor.R
import com.theapache64.tracktor.databinding.ActivityUsersBinding
import com.theapache64.twinkill.ui.activities.base.BaseAppCompatActivity
import com.theapache64.twinkill.utils.extensions.bindContentView
import dagger.android.AndroidInjection
import javax.inject.Inject

class UsersActivity : BaseAppCompatActivity(), UsersHandler {

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, UsersActivity::class.java).apply {
                // add data here
            }
        }
    }

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        val binding = bindContentView<ActivityUsersBinding>(R.layout.activity_users)
        setSupportActionBar(binding.toolbar)

        this.viewModel = ViewModelProvider(this, factory).get(UsersViewModel::class.java)
        binding.viewModel = viewModel
        binding.handler = this



        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_users, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}
