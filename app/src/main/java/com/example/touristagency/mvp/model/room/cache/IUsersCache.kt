package com.example.touristagency.mvp.model.room.cache

import com.example.touristagency.mvp.model.users.User
import io.reactivex.rxjava3.core.Single

interface IUsersCache {
    fun insertUsersIfOnline(
        users: List<User>
    ): Single<List<User>>

    fun getUsersIfOffline(
    ): Single<List<User>>
}