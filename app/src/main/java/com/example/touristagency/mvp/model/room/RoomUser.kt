package com.example.touristagency.mvp.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomUser(
    @PrimaryKey var id: String,
    var firstName: String,
    var lastName: String,
    var middleName: String,
    var birthdate: String,
    var login: String,
    var email: String,
    var password: String
)
