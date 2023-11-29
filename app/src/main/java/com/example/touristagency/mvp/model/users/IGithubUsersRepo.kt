package com.example.touristagency.mvp.model.users

import io.reactivex.rxjava3.core.Single

interface IGithubUsersRepo {
    fun getUsers(): Single<List<User>>
}