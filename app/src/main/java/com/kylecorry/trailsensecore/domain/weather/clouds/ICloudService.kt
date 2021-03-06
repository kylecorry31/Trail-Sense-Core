package com.kylecorry.trailsensecore.domain.weather.clouds

import com.kylecorry.trailsensecore.domain.geo.Coordinate

interface ICloudService {
    fun getCloudPrecipitation(cloud: CloudType): CloudWeather
    fun getCloudHeightRange(height: CloudHeight, location: Coordinate): HeightRange
    fun getCloudsByShape(shape: CloudShape): List<CloudType>
    fun getCloudsByHeight(height: CloudHeight): List<CloudType>
    fun getCloudsByColor(color: CloudColor): List<CloudType>
}