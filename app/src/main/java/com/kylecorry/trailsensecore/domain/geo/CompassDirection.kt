package com.kylecorry.trailsensecore.domain.geo

enum class CompassDirection(val azimuth: Float) {
    North(0f),
    NorthEast(45f),
    East(90f),
    SouthEast(135f),
    South(180f),
    SouthWest(225f),
    West(270f),
    NorthWest(315f)

}
