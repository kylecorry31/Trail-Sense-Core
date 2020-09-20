package com.kylecorry.trailsensecore.domain.navigation

import android.location.Location
import com.kylecorry.trailsensecore.domain.geo.Bearing
import com.kylecorry.trailsensecore.domain.geo.Coordinate
import org.junit.Test

import org.junit.Assert.*

class NavigationServiceTest {

    private val service = NavigationService()

    @Test
    fun navigate() {
        val start = Coordinate(0.0, 1.0)
        val end = Coordinate(10.0, -8.0)

        val vector = service.navigate(start, end, 0f, true)

        val expected = FloatArray(3)
        Location.distanceBetween(0.0, 1.0, 10.0, -8.0, expected)

        assertEquals(Bearing(expected[1]).value, vector.direction.value, 0.005f)
        assertEquals(expected[0], vector.distance, 0.005f)
    }

    @Test
    fun nearby() {
        val mtWashington = Coordinate(44.2706, -71.3036)
        val beacons = listOf(
            Beacon(0, "Tip top house", Coordinate(44.2705, -71.3036)),
            Beacon(1, "Crawford", Coordinate(44.2709, -71.3056)),
            Beacon(2, "Pinkham", Coordinate(44.2571, -71.2530))
        )

        val near5km = service.nearby(mtWashington, beacons, 5000f).map { it.id }
        val near500m = service.nearby(mtWashington, beacons, 500f).map { it.id }

        assertEquals(listOf(0, 1, 2), near5km)
        assertEquals(listOf(0, 1), near500m)
    }

    @Test
    fun eta(){
        val location = Coordinate(44.2571, -71.2530)
        val speed = 1.5f
        val altitude = 1000f

        val destination = Coordinate(44.2706, -71.3036)
        val destinationAltitude = 1900f
        val beacon = Beacon(0, "", destination, elevation = destinationAltitude)

        val linearEta = service.eta(Position(location, altitude, Bearing(0f), speed), beacon, false)
        val nonLinearEta = service.eta(Position(location, altitude, Bearing(0f), speed), beacon, true)

        val linearEtaDownhill = service.eta(Position(location, destinationAltitude, Bearing(0f), speed), beacon.copy(elevation = altitude), false)
        val nonLinearEtaDownhill = service.eta(Position(location, destinationAltitude, Bearing(0f), speed), beacon.copy(elevation = altitude), true)

        assertEquals(137L, linearEta.toMinutes())
        assertEquals(165L, nonLinearEta.toMinutes())
        assertEquals(47L, linearEtaDownhill.toMinutes())
        assertEquals(75L, nonLinearEtaDownhill.toMinutes())
    }
}