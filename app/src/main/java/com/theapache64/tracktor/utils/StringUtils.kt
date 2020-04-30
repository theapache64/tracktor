package com.theapache64.tracktor.utils

fun String.toReadable(): String {
    return this.replace(
        String.format(
            "%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
        ).toRegex(),
        " "
    )
}