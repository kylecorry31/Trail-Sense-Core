package com.kylecorry.trailsensecore.domain.navigation

import android.location.Location
import com.kylecorry.trailsensecore.domain.geo.Bearing
import com.kylecorry.trailsensecore.domain.geo.Coordinate
import org.junit.Test

import org.junit.Assert.*

class NavigationServiceTest {

    @Test
    fun navigate() {
        val start = Coordinate(0.0, 1.0)
        val end = Coordinate(10.0, -8.0)
        val service = NavigationService()

        val vector = service.navigate(start, end, 0f, true)

        val expected = FloatArray(3)
        Location.distanceBetween(0.0, 1.0, 10.0, -8.0, expected)

        assertEquals(Bearing(expected[1]).value, vector.direction.value, 0.005f)
        assertEquals(expected[0], vector.distance, 0.005f)
    }
}