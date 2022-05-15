package com.amary.sisosmed.presentation.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import com.amary.sisosmed.R
import com.amary.sisosmed.presentation.ui.main.map.MapsFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions

fun setMapStyle(context: Context, googleMap: GoogleMap){
    try {
        val nightMode =
            context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        val success = if (nightMode == Configuration.UI_MODE_NIGHT_YES)
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style_night
                )
            )
        else googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context,
                R.raw.map_style_light
            )
        )

        if (!success) {
            Log.e(MapsFragment::class.java.simpleName, "Style parsing failed.")
        }
    } catch (exception: Resources.NotFoundException) {
        Log.e(MapsFragment::class.java.simpleName, "Can't find style. Error: ", exception)
    }
}