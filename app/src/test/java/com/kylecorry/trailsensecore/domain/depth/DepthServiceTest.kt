package com.kylecorry.trailsensecore.domain.depth

import com.kylecorry.trailsensecore.domain.units.Distance
import com.kylecorry.trailsensecore.domain.units.DistanceUnits
import com.kylecorry.trailsensecore.domain.units.Pressure
import com.kylecorry.trailsensecore.domain.units.PressureUnits
import org.junit.Assert
import org.junit.Test

internal class DepthServiceTest {
    @Test
    fun canCalculateDepth(){
        val currentPressure = Pressure(2222.516f, PressureUnits.Hpa)
        val service = DepthService()

        val depth = service.calculateDepth(currentPressure, Pressure(1013f, PressureUnits.Hpa))

        val expected = Distance(12f, DistanceUnits.Meters)

        Assert.assertEquals(expected.distance, depth.distance, 0.1f)
    }

    @Test
    fun returnsZeroWhenAboveWater(){
        val currentPressure = Pressure(1000f, PressureUnits.Hpa)
        val service = DepthService()

        val depth = service.calculateDepth(currentPressure,  Pressure(1013f, PressureUnits.Hpa))

        val expected = Distance(0f, DistanceUnits.Meters)

        Assert.assertEquals(expected, depth)
    }
}