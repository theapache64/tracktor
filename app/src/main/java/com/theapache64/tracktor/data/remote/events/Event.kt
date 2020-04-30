package com.theapache64.tracktor.data.remote.events

import com.squareup.moshi.Json


data class Event(
    @Json(name = "id")
    val id: String, // 12162406206
    @Json(name = "type")
    val type: String, // ReleaseEvent
    @Json(name = "actor")
    val actor: Actor,
    @Json(name = "repo")
    val repo: Repo,
    @Json(name = "payload")
    val payload: Any,
    @Json(name = "public")
    val `public`: Boolean, // true
    @Json(name = "created_at")
    val createdAt: String // 2020-04-26T20:59:47Z
) {
    data class Actor(
        @Json(name = "id")
        val id: Int, // 9678279
        @Json(name = "login")
        val login: String, // theapache64
        @Json(name = "display_login")
        val displayLogin: String, // theapache64
        @Json(name = "gravatar_id")
        val gravatarId: String,
        @Json(name = "url")
        val url: String, // https://api.github.com/users/theapache64
        @Json(name = "avatar_url")
        val avatarUrl: String // https://avatars.githubusercontent.com/u/9678279?
    )

    data class Repo(
        @Json(name = "id")
        val id: Int, // 256630741
        @Json(name = "name")
        val name: String, // theapache64/topcorn
        @Json(name = "url")
        val url: String // https://api.github.com/repos/theapache64/topcorn
    )
}
