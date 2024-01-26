package com.example.touristagency.mvp.model.tours

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tour(
    @Expose val id: String? = null,
    @Expose val city: String? = null,
    @Expose val name: String? = null,
    @Expose val place: String? = null,
    @Expose val price: String? = null,

    @SerializedName("categoryId")
    @Expose val categoryId: String? = null,

    @SerializedName("airportDistance")
    @Expose val airportDistance: String? = null,

    @SerializedName("beachDistance")
    @Expose val beachDistance: String? = null,

    @Expose val rating: String? = null,
    @Expose val parking: String? = null,
    @Expose val stars: String? = null,

    @SerializedName("foodSystem")
    @Expose val foodSystem: String? = null,

    @SerializedName("foodType")
    @Expose val foodType: String? = null,

    @Expose val photo1: String? = null,
    @Expose val photo2: String? = null,
    @Expose val photo3: String? = null,

    @Expose val wifi: String? = null,
    @SerializedName("lineNumber")
    @Expose val lineNumber: Int? = null


    ) : Parcelable

