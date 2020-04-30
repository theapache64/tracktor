package com.theapache64.tracktor.ui.activities.users


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.data.repositories.UserRepo
import com.theapache64.twinkill.logger.info
import com.theapache64.twinkill.network.utils.Resource
import com.theapache64.twinkill.ui.base.BaseViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import com.theapache64.twinkill.utils.test.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class UsersViewModel @Inject constructor(
    private val userRepo: UserRepo
) : BaseViewModel() {

    class AddUserRequest(val username: String)

    companion object {
        const val STATUS_INVALID_USER = 321
        private const val STATUS_USER_EXIST = 123
    }


    val isNoUserVisible = ObservableBoolean()
    val isUsersVisible = ObservableBoolean()

    val onAddUserChannel = ConflatedBroadcastChannel<AddUserRequest>()
    val addUserResponse = onAddUserChannel.asFlow()
        .distinctUntilChanged()
        .flatMapLatest { request ->
            EspressoIdlingResource.countingIdlingResource.increment()

            // Checking if the user already exist in db
            val dbUser = userRepo.getUserLocal(request.username)

            if (dbUser != null) {
                // exist in db
                flowOf(Resource.error("${request.username} already exist", STATUS_USER_EXIST))
            } else {
                // not exist in db
                userRepo.getUserRemote(request.username)
            }

        }
        .onEach {
            when (it.status) {

                Resource.Status.LOADING -> {
                    isNoUserVisible.set(false)
                    isUsersVisible.set(false)
                }

                Resource.Status.SUCCESS -> {

                    EspressoIdlingResource.countingIdlingResource.decrement()

                    // a valid user collected from network, so add it to the db
                    val newUserEntity = it.data!!.let { user ->
                        UserEntity(
                            user.login,
                            user.avatarUrl,
                            user.type,
                            user.name
                        )
                    }

                    userRepo.saveUser(newUserEntity)

                    isNoUserVisible.set(false)
                    isUsersVisible.set(true)
                }

                Resource.Status.ERROR -> {

                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.countingIdlingResource.decrement()
                    }

                    val msg: Any = if (it.statusCode == 404) {
                        R.string.users_error_invalid_user
                    } else {
                        it.message!!
                    }

                    snackBarMessage.postValue(msg)

                    val isUsersEmpty = users.value?.isEmpty() ?: true
                    if (isUsersEmpty) {
                        // no users
                        isUsersVisible.set(false)
                        isNoUserVisible.set(true)
                    } else {
                        isUsersVisible.set(true)
                        isNoUserVisible.set(false)
                    }
                }
            }
        }
        .asLiveData()

    val users = userRepo.getUsers()
        .onEach {
            info(" YEY : Receiving users updates... ${it.size} users")

            if (it.isEmpty()) {
                isNoUserVisible.set(true)
                isUsersVisible.set(false)
            } else {
                isNoUserVisible.set(false)
                isUsersVisible.set(true)
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    private val _addUserClick = SingleLiveEvent<Boolean>()
    val addUserClick: LiveData<Boolean> = _addUserClick

    fun onAddUserClicked() {
        this._addUserClick.value = true
    }

    fun addUser(username: String) {
        onAddUserChannel.offer(AddUserRequest(username))
    }

}