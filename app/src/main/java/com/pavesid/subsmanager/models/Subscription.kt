package com.pavesid.subsmanager.models

import androidx.annotation.DrawableRes

data class Subscription(
    val title: String,
    val started: String,
    val ended: String,
    val price: String,
    @DrawableRes val color: Int = 0,
    val image: Int = 0,
    val type: Int = 0,
    val needPrice: Float = 0f,
    val id: Int = -1
)