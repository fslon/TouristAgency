package com.example.touristagency.mvp.model.users

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @Expose val id: String? = null,
    @Expose val firstName: String? = null,
    @Expose val lastName: String? = null,
    @Expose val middleName: String? = null,
    @Expose val birthdate: String? = null,
    @Expose val login: String? = null,
    @Expose val email: String? = null,
    @Expose val password: String? = null


) : Parcelable
