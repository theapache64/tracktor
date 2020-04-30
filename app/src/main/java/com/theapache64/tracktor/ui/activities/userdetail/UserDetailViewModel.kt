package com.theapache64.tracktor.ui.activities.userdetail

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.local.entities.UserEntity
import com.theapache64.tracktor.data.repositories.EventsRepo
import com.theapache64.tracktor.data.repositories.UserRepo
import com.theapache64.tracktor.models.UserEvent
import com.theapache64.twinkill.logger.info
import com.theapache64.twinkill.network.utils.Resource
import com.theapache64.twinkill.utils.livedata.SingleLiveEvent
import com.theapache64.twinkill.utils.test.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.theapache64.twinkill.utils.Resource as LocalResource

@FlowPreview
@ExperimentalCoroutinesApi
class UserDetailViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val eventsRepo: EventsRepo
) : ViewModel() {

    private val _viewUrl = MutableLiveData<String>()
    val viewUrl: LiveData<String> = _viewUrl
    private val _loadingView = MutableLiveData<LocalResource<Any>>()
    val loadingView: LiveData<com.theapache64.twinkill.utils.Resource<Any>> = _loadingView
    val deleteUserDialog = SingleLiveEvent<Unit>()
    val finishActivity = SingleLiveEvent<Unit>()

    private val userIdChannel = ConflatedBroadcastChannel<Long>()
    private val userChannel = ConflatedBroadcastChannel<UserEntity>()

    val events: LiveData<List<UserEvent>> = userChannel.asFlow()
        .distinctUntilChanged()
        .flatMapLatest { user ->
            info("New user requesting for events")
            eventsRepo.getUserEvents(user.username)
        }
        .filter { response ->
            when (response.status) {
                Resource.Status.LOADING -> {
                    EspressoIdlingResource.countingIdlingResource.increment()
                    isUserEventsVisible.set(false)
                    _loadingView.value = LocalResource.loading(R.string.user_detail_loading_events)
                    false
                }
                Resource.Status.SUCCESS -> {
                    EspressoIdlingResource.countingIdlingResource.decrement()
                    isUserEventsVisible.set(true)
                    _loadingView.value = LocalResource.success(null)
                    true // only pass success
                }
                Resource.Status.ERROR -> {
                    EspressoIdlingResource.countingIdlingResource.decrement()
                    isUserEventsVisible.set(false)
                    _loadingView.value = LocalResource.error(response.message!!)
                    false
                }
            }
        }
        .map { it.data!! }
        .asLiveData(viewModelScope.coroutineContext)


    val user = userIdChannel.asFlow()
        .flatMapLatest { userId -> userRepo.getUserLocal(userId) }
        .onEach { user ->
            if (user != null) {

                // got user
                userChannel.offer(user) // load user events
            }
        }
        .asLiveData(viewModelScope.coroutineContext)

    val isUserEventsVisible = ObservableBoolean()

    fun onDeleteIconClicked() {
        deleteUserDialog.call()
    }

    fun onCloseClicked() {
        finishActivity.call()
    }

    fun init(userId: Long) {

        viewModelScope.launch {
            // updating user score
            userRepo.incrementUserScore(userId)
        }

        userIdChannel.offer(userId)
    }

    fun performDeleteUser() {
        viewModelScope.launch {
            val isDeleted = userRepo.deleteUser(user.value!!) == 1
            require(isDeleted) { "Failed to delete user" }
            finishActivity.call()
        }
    }

    fun onEventClicked(userEvent: UserEvent) {
        _viewUrl.value = userEvent.url
    }

    fun onEventDetailClicked(event: UserEvent, detail: UserEvent.Detail) {
        if (detail.url != null) {
            _viewUrl.value = detail.url
        } else {
            onEventClicked(event)
        }
    }
}