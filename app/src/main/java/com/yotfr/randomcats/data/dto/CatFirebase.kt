package com.yotfr.randomcats.data.dto

import java.text.SimpleDateFormat
import java.util.*

data class CatFirebase(
    val id:String = "",
    val url:String = "",
    val created:Long = 0L,
    val createdDateString: String = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
        created
    )
)