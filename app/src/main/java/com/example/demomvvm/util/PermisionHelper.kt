package com.example.demomvvm.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


object PermisionHelper {

    lateinit var permisionResult: PermisionResult

    fun askPermision(
        context: Context,
        appPermissions: Array<String>,
        permisionResult: PermisionResult
    ) {
        PermisionHelper.permisionResult = permisionResult

        ActivityCompat.requestPermissions(
            context as Activity,
            appPermissions,
            PERMISSIONS_REQUEST_CODE
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            var deniedCount = 0;
            // Gather permission grant results
            for (i in grantResults) {
                // Add only permissions which are denied
                if (i == PackageManager.PERMISSION_DENIED) {
                    deniedCount++;
                }
            }

            // Check if all permissions are granted
            if (deniedCount == 0) {
                // Proceed ahead with the app
                permisionResult.grantPermision()
            } else {
                permisionResult.deniedpermision()
            }
        }

    }

    interface PermisionResult {
        fun grantPermision()
        fun deniedpermision()
    }

}