package com.lordsandlegends.crew.data

import android.net.Uri


//stores all the variables that all the other files pass around and use to send information
//through
data class Contract(
    val id: String,
    val fileName: String,
    val fileUri: Uri,
    var signed: Boolean = false,
    var signedAt: Long? = null,
)