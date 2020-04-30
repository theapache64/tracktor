package com.theapache64.tracktor.models

import androidx.annotation.StringRes

class UserEvent(
    val emoji: String,
    val title: String,
    val details: List<Detail>,
    val url: String,
    val createdAt: Long
) {
    class Detail(
        @StringRes
        val key: Int,
        val value: String,
        val url: String? = null
    )
}