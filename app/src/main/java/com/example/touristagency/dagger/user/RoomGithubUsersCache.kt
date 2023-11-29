package com.example.touristagency.dagger.user

import com.example.touristagency.mvp.model.room.Database
import com.example.touristagency.mvp.model.room.RoomGithubUser
import com.example.touristagency.mvp.model.room.cache.IUsersCache
import com.example.touristagency.mvp.model.users.User
import io.reactivex.rxjava3.core.Single

class RoomGithubUsersCache(val db: Database) : IUsersCache {
    override fun insertUsersIfOnline(users: List<User>): Single<List<User>> {
        return Single.fromCallable {
            val roomUsers = users.map { user ->
                RoomGithubUser(
                    user.id ?: "", user.firstName ?: "", user.lastName ?: "",
                    user.middleName ?: "", user.birthdate ?: "", user.login ?: "", user.email ?: "", user.password ?: "",
                )

            }
            db.userDao.insert(roomUsers)
            return@fromCallable users
        }
    }

    override fun getUsersIfOffline(): Single<List<User>> {

        return Single.fromCallable {
            db.userDao.getAll().map { roomUser ->
                User(
                    roomUser.id, roomUser.firstName, roomUser.lastName,
                    roomUser.middleName, roomUser.birthdate, roomUser.login, roomUser.email, roomUser.password,
                )
            }
        }
    }
}