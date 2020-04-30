package com.theapache64.tracktor.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.winterbe.expekt.should
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DateTimeUtilsTest {

    @Test
    fun given5Minutes_whenToRelativeTimeString_then5minutesAgo() {

        // given : Creating input data with whenPassedTimeStamp
        val minutesPassed: Long = 5
        val timeStamp = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(minutesPassed)

        // when : Calling subject -> givenTimeStamp
        val actualResult = DateTimeUtils.toRelativeTimeString(timeStamp)
        val expectedResult = "$minutesPassed minutes ago"

        actualResult.should.equal(expectedResult)
    }

    @Test
    fun givenISO8601Format_whenConvertedToLocal_thenTimeMatch() {
        val utcTime = "2020-04-29T06:29:16Z"
        val localTime = Date(DateTimeUtils.utcToLocal(utcTime))
        localTime.toString().should.equal("Wed Apr 29 11:59:16 IST 2020")
    }
}