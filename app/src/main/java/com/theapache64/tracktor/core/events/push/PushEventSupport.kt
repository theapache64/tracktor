package com.theapache64.tracktor.core.events.push

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.R
import com.theapache64.tracktor.core.events.BaseEventSupport
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent

class PushEventSupport(moshi: Moshi, event: Event) :
    BaseEventSupport<PushEventPayload>(moshi, event, PushEventPayload::class.java) {

    override fun getEmoji(): String = "👐"

    override fun getTitle(event: Event): String = "Commits pushed"

    override fun getEvenDetails(event: Event): List<UserEvent.Detail>? {
        val details = mutableListOf<UserEvent.Detail>()
        details.add(
            UserEvent.Detail(
                R.string.user_detail_event_detail_key_commit_count,
                payload!!.size.toString()
            )
        )

        // Adding commits
        for (commit in payload.commits) {

            val url = "https://github.com/${event.repo.name}/commit/${commit.sha}"

            val commitDetail = UserEvent.Detail(
                R.string.user_detail_event_detail_key_commit,
                commit.message,
                url
            )

            details.add(commitDetail)
        }

        return details
    }

    override fun getTargetUrl(event: Event): String? = null
}