package com.example.touristagency.mvp.model.users

import retrofit2.Call

class RetrofitUsersRepo(
    val api: IDataSourceUser
//    val api: IDataSource, val networkStatus: INetworkStatus
) : IUsersRepo {


    override fun login(login: String, password: String): Call<Any> {
        return api.loginUser(login, password)
    }

    override fun register(
        login: String,
        password: String,
        email: String,
        birthdate: String,
        name: String,
        lastname: String,
        surname: String
    ): Call<Any> {
        return api.registerUser(login, password, email, birthdate, name, lastname, surname)
    }

//    override fun getTours() = networkStatus.isOnlineSingle().flatMap { isOnline
//        ->
//        api.getTours()
//            .flatMap { tours ->
//                cacheInterface.insertTours(tours)
//            }
//    }.subscribeOn(Schedulers.io())


}