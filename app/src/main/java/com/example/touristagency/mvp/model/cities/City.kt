package com.example.touristagency.mvp.model.cities

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    @Expose val id: String? = null,
    @SerializedName("cityName")
    @Expose val cityName: String? = null
    ) : Parcelable

