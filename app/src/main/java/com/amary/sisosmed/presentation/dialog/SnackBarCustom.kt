package com.amary.sisosmed.presentation.dialog

import android.app.Activity
import androidx.core.content.ContextCompat
import com.amary.sisosmed.R
import com.google.android.material.snackbar.Snackbar

class SnackBarCustom(private val activity: Activity) {
    private fun setup(message: String, background: Int): Snackbar {
        return Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(activity, background))
    }

    fun make(isSuccess: Boolean = false, message: String): Snackbar {
        if (isSuccess){
            return setup(message, R.color.success)
        }

        return setup(message, R.color.danger)
    }
}