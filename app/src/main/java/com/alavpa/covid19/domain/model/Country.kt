package com.alavpa.covid19.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    @SerializedName("Country")
    val country: String = "",
    @SerializedName("Slug")
    val slug: String = ""
): Parcelable