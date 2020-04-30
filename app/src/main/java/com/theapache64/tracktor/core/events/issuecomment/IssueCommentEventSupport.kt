package com.theapache64.tracktor.core.events.issuecomment

import com.squareup.moshi.Moshi
import com.theapache64.tracktor.R
import com.theapache64.tracktor.core.events.BaseEventSupport
import com.theapache64.tracktor.data.remote.events.Event
import com.theapache64.tracktor.models.UserEvent

class IssueCommentEventSupport(moshi: Moshi, event: Event) :
    BaseEventSupport<IssueCommentEventPayload>(moshi, event, IssueCommentEventPayload::class.java) {

    companion object {
        private const val COMMENT_BODY_LIMIT = 150
    }

    override fun getEmoji(): String = "💬"

    override fun getTitle(event: Event): String =
        "${payload!!.action.capitalize()} an issue comment"

    override fun getEvenDetails(event: Event): List<UserEvent.Detail>? {
        val comment = payload!!.comment
        var body = comment.body
        if (body.length > COMMENT_BODY_LIMIT) {
            body = "${body.substring(0, COMMENT_BODY_LIMIT)}..."
        }
        return listOf(
            UserEvent.Detail(
                R.string.user_detail_event_detail_key_comment,
                body,
                comment.htmlUrl
            )
        )
    }

    override fun getTargetUrl(event: Event): String? = null
}