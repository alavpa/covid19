package com.alavpa.covid19.domain.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Case(
    @SerializedName("Country")
    val country: String = "",
    @SerializedName("Province")
    val province: String = "",
    @SerializedName("Date")
    val date: Date? = null,
    @SerializedName("Cases")
    val cases: Int = 0
)