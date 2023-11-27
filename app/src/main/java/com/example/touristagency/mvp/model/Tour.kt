package com.example.touristagency.mvp.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tour(
    @Expose val id: String? = null,
    @Expose val login: String? = null,
    @Expose val avatarUrl: String? = null,

    @Expose val picture1: String? = null,
    @Expose val picture2: String? = null,
    @Expose val picture3: String? = null,
    @Expose val picture4: String? = null,
    @Expose val picture5: String? = null,
    @Expose val picture6: String? = null,
    @Expose val picture7: String? = null,
    @Expose val picture8: String? = null,
    @Expose val picture9: String? = null,
    @Expose val picture10: String? = null

    // todo добавить еще поля

) : Parcelable

