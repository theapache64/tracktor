package com.theapache64.tracktor.utils

import com.winterbe.expekt.should
import org.junit.Test

class StringUtilsKtTest {
    @Test
    fun testToReadable() {
        "TheCar".toReadable().should.equal("The Car")
    }
}