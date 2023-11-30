package com.example.touristagency.mvp.model.users

import com.example.touristagency.mvp.model.room.Database
import com.example.touristagency.mvp.model.room.cache.IUsersCache
import com.example.touristagency.ui.network.INetworkStatus
import io.reactivex.rxjava3.schedulers.Schedulers

class RetrofitGithubUsersRepo(
    val api: IDataSource, val networkStatus: INetworkStatus, val db: Database, val cacheInterface: IUsersCache
//    val api: IDataSource, val networkStatus: INetworkStatus
) : IToursAndUsersRepo {
    override fun getUsers() = networkStatus.isOnlineSingle().flatMap { isOnline
        ->
        if (isOnline) {
            api.getUsers()
                .flatMap { users ->
                    cacheInterface.insertUsersIfOnline(users)
                }
        } else {
            cacheInterface.getUsersIfOffline()
        }
    }.subscribeOn(Schedulers.io())
}