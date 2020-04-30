package com.theapache64.tracktor.core.events

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.core.events.issuecomment.IssueCommentEventSupport
import com.theapache64.tracktor.core.events.issues.IssuesEventSupport
import com.theapache64.tracktor.core.events.push.PushEventSupport
import com.theapache64.tracktor.core.events.watch.WatchEventSupport
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent
import com.theapache64.tracktor.utils.DateTimeUtils
import com.theapache64.tracktor.utils.htmlUrl
import com.theapache64.tracktor.utils.toReadable
import com.theapache64.tracktor.utils.userEventDetail

object EventManager {

    fun convertToUserEvent(
        moshi: Moshi,
        event: Event
    ): UserEvent {

        val eventSupport = when (event.type) {
            "IssuesEvent" -> IssuesEventSupport(
                moshi,
                event
            )
            "WatchEvent" -> WatchEventSupport(
                moshi,
                event
            )

            "PushEvent" -> PushEventSupport(moshi, event)

            "IssueCommentEvent" -> IssueCommentEventSupport(moshi, event)

            else -> null
        }

        return UserEvent(
            eventSupport?.getEmoji() ?: "⌨️",
            eventSupport?.getTitle(event) ?: event.type.toReadable(),
            listOf(
                event.repo.userEventDetail(),
                *(eventSupport?.getEvenDetails(event) ?: listOf()).toTypedArray()
            ),
            eventSupport?.getTargetUrl(event) ?: event.repo.htmlUrl(),
            DateTimeUtils.utcToLocal(event.createdAt)
        )
    }
}