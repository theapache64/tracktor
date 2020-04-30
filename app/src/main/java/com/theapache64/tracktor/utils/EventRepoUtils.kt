package com.theapache64.tracktor.utils

import com.theapache64.tracktor.R
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent

fun Event.Repo.htmlUrl(): String {
    return "https://github.com/$name"
}

fun Event.Repo.userEventDetail(): UserEvent.Detail {
    return UserEvent.Detail(
        R.string.user_detail_event_detail_key_repo,
        name
    )
}