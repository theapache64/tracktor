package com.theapache64.tracktor.ui.activities.users


import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.data.remote.user.User
import com.theapache64.tracktor.data.repositories.UserRepo
import com.theapache64.twinkill.logger.info
import com.theapache64.twinkill.network.utils.Resource
import com.theapache64.twinkill.ui.base.BaseViewModel
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import com.theapache64.twinkill.utils.test.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@FlowPreview
@ExperimentalCoroutinesApi
class UsersViewModel @Inject constructor(
    private val userRepo: UserRepo
) : BaseViewModel() {

    companion object {
        const val STATUS_INVALID_USER = 321
        private const val STATUS_USER_EXIST = 123
    }


    val isNoUserVisible = ObservableBoolean()
    val isUsersVisible = ObservableBoolean()

    val onAddUserChannel = MutableLiveData<String>()
    val addUserResponse = onAddUserChannel.switchMap { username ->
        liveData<Resource<User>> {

            EspressoIdlingResource.countingIdlingResource.increment()
            info("Checking for add new user... $username")

            // Checking if the user already exist in db
            val dbUser = userRepo.getUserLocal(username)

            if (dbUser != null) {
                // exist in db
                EspressoIdlingResource.countingIdlingResource.decrement()
                emit(Resource.error("$username already exist", STATUS_USER_EXIST))
            } else {
                // not exist in db
                val remoteUsername = userRepo.getUserRemote(username)
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

                                EspressoIdlingResource.countingIdlingResource.decrement()

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

                emitSource(remoteUsername)
            }
        }
    }

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
        info("Calling add new user")
        onAddUserChannel.value = username
    }

}