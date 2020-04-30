package com.theapache64.tracktor.data.remote.user

import com.squareup.moshi.Json

/**
 * Using same model for both retrofit and Room
 */
data class User(
    @Json(name = "login")
    val login: String, // theapache64

    @Json(name = "avatar_url")
    val avatarUrl: String, // https://avatars1.githubusercontent.com/u/9678279?v=4

    @Json(name = "type")
    val type: String, // User

    @Json(name = "name")
    val name: String // theapache64
)