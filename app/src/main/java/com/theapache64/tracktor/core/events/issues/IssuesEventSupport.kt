package com.theapache64.tracktor.core.events.issues

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.R
import com.theapache64.tracktor.core.events.BaseEventSupport
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent

class IssuesEventSupport(moshi: Moshi, event: Event) :
    BaseEventSupport<IssuesEventPayload>(moshi, event, IssuesEventPayload::class.java) {


    override fun getEmoji(): String = "🐞"

    override fun getTitle(event: Event): String =
        "${payload!!.action.capitalize()} an issue"

    override fun getEvenDetails(
        event: Event
    ): List<UserEvent.Detail> =
        listOf(
            UserEvent.Detail(
                R.string.user_detail_event_detail_key_action,
                payload!!.action
            )
        )

    override fun getTargetUrl(event: Event) = payload!!.issue.htmlUrl
}