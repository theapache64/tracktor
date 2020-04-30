package com.theapache64.tracktor.core.events.watch

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.core.events.BaseEventSupport
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent

class WatchEventSupport(moshi: Moshi, event: Event) :
    BaseEventSupport<Void>(moshi, event, null) {
    override fun getEmoji(): String = "🌟"
    override fun getTitle(event: Event): String = "Stared a repo"
    override fun getEvenDetails(event: Event): List<UserEvent.Detail>? = null
    override fun getTargetUrl(event: Event): String? = null
}
