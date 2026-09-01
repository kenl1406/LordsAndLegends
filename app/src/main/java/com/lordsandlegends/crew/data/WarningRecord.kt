package com.lordsandlegends.crew.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.geometry.Offset

data class WarningRecord(
    val name: String,
    val surname: String,
    val date: String,
    val reason: String,
    val type: String, // Warning or Performance Note
    val managerSignature: Bitmap? = null,
    var signed: Boolean = false,
    var signedAt: Long? = null,
)

