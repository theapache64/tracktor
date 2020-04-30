package com.theapache64.tracktor.core.events

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent

abstract class BaseEventSupport<T>(
    moshi: Moshi,
    event: Event,
    type: Class<T>?
) {

    val payload: T? = if (type != null) {
        moshi.adapter(type).fromJsonValue(event.payload)
    } else {
        null
    }

    abstract fun getEmoji(): String
    abstract fun getTitle(event: Event): String
    abstract fun getEvenDetails(event: Event): List<UserEvent.Detail>?
    abstract fun getTargetUrl(event: Event): String?
}