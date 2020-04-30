package com.theapache64.tracktor.data.repositories

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.core.events.EventManager
import com.theapache64.tracktor.data.remote.ApiInterface
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent
import com.theapache64.twinkill.network.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventsRepo @Inject constructor(
    private val moshi: Moshi,
    private val apiInterface: ApiInterface
) {
    fun getEvents(username: String): Flow<Resource<List<Event>>> = apiInterface.getEvents(username)
    fun getUserEvents(username: String): Flow<Resource<List<UserEvent>>> = getEvents(username)
        .map {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Resource.loading()
                }
                Resource.Status.SUCCESS -> {
                    val userEvents = it.data!!.map { event ->
                        EventManager.convertToUserEvent(moshi, event)
                    }

                    Resource.success(userEvents, it.statusCode)
                }
                Resource.Status.ERROR -> {
                    Resource.error(
                        it.message!!,
                        it.statusCode,
                        null
                    )
                }
            }
        }
}