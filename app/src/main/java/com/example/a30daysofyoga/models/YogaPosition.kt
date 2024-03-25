package com.example.a30daysofyoga.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class YogaPosition(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int
)
