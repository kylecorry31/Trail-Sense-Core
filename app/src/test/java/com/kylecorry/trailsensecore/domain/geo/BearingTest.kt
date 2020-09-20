package com.kylecorry.trailsensecore.domain.geo

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.math.exp

internal class BearingTest {

    @ParameterizedTest
    @MethodSource("provideDirection")
    fun getDirection(azimuth: Float, expected: CompassDirection) {
        val bearing = Bearing(azimuth)
        assertEquals(expected, bearing.direction)
    }

    @ParameterizedTest
    @MethodSource("provideAzimuth")
    fun value(azimuth: Float, expected: Float){
        val bearing = Bearing(azimuth)
        assertEquals(expected, bearing.value, 0.01f)
    }

    @Test
    fun withDeclination() {
        val bearing = Bearing(45f)
        val dec = bearing.withDeclination(-10f)
        assertEquals(35f, dec.value, 0.01f)
    }

    companion object {
        @JvmStatic
        fun provideDirection(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0f, CompassDirection.North),
                Arguments.of(90f, CompassDirection.East),
                Arguments.of(180f, CompassDirection.South),
                Arguments.of(270f, CompassDirection.West),

                Arguments.of(45f, CompassDirection.NorthEast),
                Arguments.of(135f, CompassDirection.SouthEast),
                Arguments.of(225f, CompassDirection.SouthWest),
                Arguments.of(315f, CompassDirection.NorthWest),

                Arguments.of(10f, CompassDirection.North),
                Arguments.of(350f, CompassDirection.North),
            )
        }

        @JvmStatic
        fun provideAzimuth(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0f, 0f),
                Arguments.of(180f, 180f),
                Arguments.of(-10f, 350f),
                Arguments.of(-180f, 180f),
                Arguments.of(710f, 350f),
                Arguments.of(360f, 0f),
                Arguments.of(-710f, 10f)
            )
        }
    }
}