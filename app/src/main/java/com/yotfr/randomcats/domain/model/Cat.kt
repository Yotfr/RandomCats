package com.yotfr.randomcats.domain.model


data class Cat(
    val id:String,
    val url:String,
    val created:Long,
    //userId will be replaced to valid in the data layer
    val userId:String = ""
)
