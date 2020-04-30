package com.theapache64.tracktor.core.events.issuecomment

import com.squareup.moshi.Json

data class IssueCommentEventPayload(
    @Json(name = "action")
    val action: String, // created
    @Json(name = "comment")
    val comment: Comment
) {
    data class Comment(
        @Json(name = "html_url")
        val htmlUrl: String, // https://github.com/square/okhttp/issues/6007#issuecomment-621520733
        @Json(name = "body")
        val body: String // There's nothing wrong with doing this. The stdlib is forward compatible. The assumption here is that we will always release Okio near OkHttp which is unlikely to be true.It's not clear why releasing a new version of Okio would solve anything unless we also released a new version of OkHttp with that version. Otherwise you would need to depend on that new Okio explicitly which would make OkHttp and your project disagree.The whole point of version resolution semantics in tools like Aether and Gradle and semantic versioning is so that we explicitly don't need to do things like this.
    )
}