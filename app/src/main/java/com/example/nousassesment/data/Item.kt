package com.example.nousassesment.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String
): Parcelable
