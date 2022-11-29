package com.yotfr.randomcats.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Cat(
    val id:String,
    val url:String,
    val created:Long,
    val createdDateString: String = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
        created
    )
)
