package com.example.touristagency.dagger.user

import com.example.touristagency.dagger.scopes.ToursScope
import com.example.touristagency.mvp.model.room.Database
import com.example.touristagency.mvp.model.room.cache.IUsersCache
import com.example.touristagency.mvp.model.users.IDataSource
import com.example.touristagency.mvp.model.users.IToursAndUsersRepo
import com.example.touristagency.mvp.model.users.RetrofitGithubUsersRepo
import com.example.touristagency.ui.network.INetworkStatus
import dagger.Module
import dagger.Provides


@Module
class UserModule {

    @Provides
    fun usersCache(database: Database): IUsersCache =
        RoomGithubUsersCache(database)

    @ToursScope
    @Provides
    fun usersRepo(
        api: IDataSource, networkStatus: INetworkStatus, db: Database, cache:
        IUsersCache
    ): IToursAndUsersRepo = RetrofitGithubUsersRepo(api, networkStatus, db, cache)

}