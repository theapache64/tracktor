package com.theapache64.tracktor.data.remote

import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.data.remote.user.User
import com.theapache64.twinkill.network.utils.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * To hold all API methods
 */
interface ApiInterface {

    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Flow<Resource<User>>

    @GET("users/{username}/events")
    fun getEvents(
        @Path("username") username: String
    ): Flow<Resource<List<Event>>>
}

