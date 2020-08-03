package com.pavesid.subsmanager.utils

import com.pavesid.subsmanager.R
import com.pavesid.subsmanager.models.Color

object Utils {
    val COLORS: List<Color> by lazy {
        val colors = mutableListOf<Color>()

        colors.add(Color(R.drawable.blueviolet))
        colors.add(Color(R.drawable.orange))
        colors.add(Color(R.drawable.palegreen))
        colors.add(Color(R.drawable.peru))
        colors.add(Color(R.drawable.yellow))
        colors.add(Color(R.drawable.greenyellowdouble))
        colors.add(Color(R.drawable.greendoubleyellow))
        colors.add(Color(R.drawable.custom_one))
        colors.add(Color(R.drawable.custom_two))
        colors.add(Color(R.drawable.custom_three))
        colors.add(Color(R.drawable.custom_four))
        colors.add(Color(R.drawable.custom_six))
        colors.add(Color(R.drawable.custom_seven))
        colors.add(Color(R.drawable.custom_eight))

        colors
    }
}