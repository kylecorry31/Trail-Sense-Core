package com.kylecorry.trailsensecore.domain.time

import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

class DateUtilsTest {

    @Test
    fun canGetClosestPastTime() {
        val now = dt(2020, Month.JANUARY, 10, 2)
        val times = listOf(
            dt(2020, Month.JANUARY, 10, 0),
            dt(2020, Month.JANUARY, 11, 0),
            dt(2020, Month.JANUARY, 10, 1),
            null
        )

        val actual = DateUtils.getClosestPastTime(now, times)

        assertEquals(dt(2020, Month.JANUARY, 10, 1), actual)
    }

    @Test
    fun canGetClosestTime() {
        val now = zdt(2020, Month.JANUARY, 10, 2)
        val times = listOf(
            zdt(2020, Month.JANUARY, 10, 0),
            zdt(2020, Month.JANUARY, 11, 0),
            zdt(2020, Month.JANUARY, 10, 1),
            zdt(2020, Month.JANUARY, 10, 2, 30),
            null
        )

        val actual = DateUtils.getClosestTime(now, times)

        assertEquals(zdt(2020, Month.JANUARY, 10, 2, 30), actual)
    }


    @Test
    fun canGetClosestFutureTime() {
        val now = dt(2020, Month.JANUARY, 10, 2)
        val times = listOf(
            dt(2020, Month.JANUARY, 10, 0),
            dt(2020, Month.JANUARY, 11, 0),
            dt(2020, Month.JANUARY, 10, 1),
            null
        )

        val actual = DateUtils.getClosestFutureTime(now, times)

        assertEquals(dt(2020, Month.JANUARY, 11, 0), actual)
    }

    @Test
    fun returnsNullIfNoFutureTimes() {
        val now = dt(2020, Month.JANUARY, 10, 2)
        val times = listOf(
            dt(2020, Month.JANUARY, 10, 0),
            dt(2020, Month.JANUARY, 10, 1),
            null
        )

        val actual = DateUtils.getClosestFutureTime(now, times)

        assertNull(actual)
    }

    @Test
    fun returnsNullIfNoPastTimes() {
        val now = dt(2020, Month.JANUARY, 9, 2)
        val times = listOf(
            dt(2020, Month.JANUARY, 10, 0),
            dt(2020, Month.JANUARY, 10, 1),
            null
        )

        val actual = DateUtils.getClosestPastTime(now, times)

        assertNull(actual)
    }



    private fun dt(year: Int, month: Month, day: Int, hour: Int, minute: Int = 0): LocalDateTime {
        return LocalDateTime.of(year, month, day, hour, minute)
    }

    private fun zdt(year: Int, month: Month, day: Int, hour: Int, minute: Int = 0, zone: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
        return ZonedDateTime.of(dt(year, month, day, hour, minute), zone)
    }
}