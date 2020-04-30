package com.theapache64.tracktor.core.events.issues

import com.squareup.moshi.Json


data class IssuesEventPayload(
    @Json(name = "action")
    val action: String, // opened
    @Json(name = "issue")
    val issue: Issue
) {
    data class Issue(
        @Json(name = "html_url")
        val htmlUrl: String, // https://github.com/theapache64/jaba/issues/45
        @Json(name = "id")
        val id: Int, // 608689364
        @Json(name = "node_id")
        val nodeId: String, // MDU6SXNzdWU2MDg2ODkzNjQ=
        @Json(name = "number")
        val number: Int, // 45
        @Json(name = "title")
        val title: String, // Add barista
        @Json(name = "created_at")
        val createdAt: String, // 2020-04-29T00:01:50Z
        @Json(name = "author_association")
        val authorAssociation: String, // OWNER
        @Json(name = "body")
        val body: String // https://github.com/AdevintaSpain/Barista
    )
}