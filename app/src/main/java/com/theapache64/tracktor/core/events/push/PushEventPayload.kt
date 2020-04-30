package com.theapache64.tracktor.core.events.push

import com.squareup.moshi.Json

data class PushEventPayload(
    @Json(name = "size")
    val size: Int, // 2
    @Json(name = "commits")
    val commits: List<Commit>
) {
    data class Commit(
        @Json(name = "sha")
        val sha: String, // ce8a50d89d3c18407a7139b31651a76bdb1feba0
        @Json(name = "message")
        val message: String // ➕ add comment
    )
}