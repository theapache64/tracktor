package com.theapache64.tracktor.utils.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Observes a [LiveData] until the `block` is done executing.
 */
suspend fun <T> LiveData<T>.observeForTesting(block: suspend () -> Unit) {
    val observer = Observer<T> { }
    try {
        observeForever(observer)
        block()
    } finally {
        removeObserver(observer)
    }
}