package com.theapache64.tracktor.utils.test

import androidx.test.espresso.intent.Intents

fun watchIntent(function: () -> Unit) {
    Intents.init()
    function()
    Intents.release()
}