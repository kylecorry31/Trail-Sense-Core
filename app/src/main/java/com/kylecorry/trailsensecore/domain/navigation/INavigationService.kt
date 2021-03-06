package com.kylecorry.trailsensecore.domain.navigation

import com.kylecorry.trailsensecore.domain.geo.Bearing
import com.kylecorry.trailsensecore.domain.geo.Coordinate
import com.kylecorry.trailsensecore.domain.units.Distance
import java.time.Duration

interface INavigationService {

    fun triangulate(pointA: Coordinate, bearingA: Bearing, pointB: Coordinate, bearingB: Bearing): Coordinate?
    fun deadReckon(lastLocation: Coordinate, distanceTravelled: Float, bearingToLast: Bearing): Coordinate

    fun navigate(
        from: Coordinate,
        to: Coordinate,
        declination: Float,
        useTrueNorth: Boolean
    ): NavigationVector

    fun destination(from: Coordinate, distance: Float, bearing: Bearing): Coordinate

    fun navigate(
        from: Position,
        to: Beacon,
        declination: Float,
        usingTrueNorth: Boolean = true
    ): NavigationVector

    fun eta(from: Position, to: Beacon, nonLinear: Boolean): Duration

    fun nearby(location: Coordinate, beacons: List<Beacon>, maxDistance: Float): List<Beacon>

    fun getPaceDistance(paces: Int, paceLength: Distance): Distance

    fun getPaces(steps: Int): Int

    fun getPaceLength(paces: Int, distanceTravelled: Distance): Distance

}