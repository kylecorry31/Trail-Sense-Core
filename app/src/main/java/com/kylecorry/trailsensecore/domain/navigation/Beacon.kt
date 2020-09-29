package com.kylecorry.trailsensecore.domain.navigation

import com.kylecorry.trailsensecore.domain.geo.Coordinate

data class Beacon(
    override val id: Long,
    override val name: String,
    val coordinate: Coordinate,
    val visible: Boolean = true,
    val comment: String? = null,
    val beaconGroupId: Int? = null,
    val elevation: Float? = null
) : IBeacon