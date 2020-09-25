package com.kylecorry.trailsensecore.infrastructure.flashlight

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import androidx.core.content.getSystemService
import java.lang.Exception

class Flashlight(private val context: Context) : IFlashlight {

    private val cameraService by lazy { getCameraManager(context) }

    override fun on() {
        if (!hasFlashlight(context)) {
            return
        }
        try {
            cameraService?.setTorchMode(getRearCameraId(context), true)
        } catch (e: Exception) {
            // No flash, ignoring
        }
    }

    override fun off() {
        try {
            cameraService?.setTorchMode(getRearCameraId(context), false)
        } catch (e: Exception) {
            // No flash, ignoring
        }
    }

    companion object {

        private fun getCameraManager(context: Context): CameraManager? {
            return try {
                context.getSystemService()
            } catch (e: Exception) {
                null
            }
        }

        fun hasFlashlight(context: Context): Boolean {

            try {
                if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    return false
                }
            } catch (e: Exception) {
                // Could not check the package manager - do nothing
            }

            try {
                val cs = getCameraManager(context)
                val rearCamera = getRearCameraId(context)
                if (rearCamera.isEmpty() || cs == null) {
                    return false
                }

                val hasFlash = cs.getCameraCharacteristics(rearCamera)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                val facing = cs.getCameraCharacteristics(rearCamera)
                    .get(CameraCharacteristics.LENS_FACING)

                return hasFlash != null && hasFlash && facing != null && facing == CameraMetadata.LENS_FACING_BACK
            } catch (e: Exception) {
                return false
            }
        }

        private fun getRearCameraId(context: Context): String {
            val cs = getCameraManager(context)
            val cameraList = cs?.cameraIdList
            if (cameraList == null || cameraList.isEmpty()) return ""
            for (camera in cameraList) {
                val hasFlash = cs.getCameraCharacteristics(camera)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
                val facing = cs.getCameraCharacteristics(camera)
                    .get(CameraCharacteristics.LENS_FACING)
                if (hasFlash != null && hasFlash && facing != null && facing == CameraMetadata.LENS_FACING_BACK) {
                    return camera
                }

            }
            return cameraList[0]
        }
    }

}