package com.example.touristagency.mvp.model.tours

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tour(
    @Expose val id: String? = null,
    @Expose val city: String? = null,
    @Expose val name: String? = null,
    @Expose val place: String? = null,
    @Expose val price: String? = null,
    @Expose val categoryId: String? = null,
    @Expose val airportDistance: String? = null,
    @Expose val beachDistance: String? = null,
    @Expose val rating: String? = null,
    @Expose val parking: String? = null,
    @Expose val stars: String? = null,
    @Expose val foodSystem: String? = null,
    @Expose val foodType: String? = null,

    @Expose val picture1: String? = null,
    @Expose val picture2: String? = null,
    @Expose val picture3: String? = null


) : Parcelable

