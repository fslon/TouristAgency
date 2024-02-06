package com.example.touristagency.dagger.tours

import com.example.touristagency.mvp.model.hotels.Hotel
import io.reactivex.rxjava3.core.Single

class RoomHotelsCache() : IHotelsCache {
    override fun insertTours(tours: List<Hotel>): Single<List<Hotel>> {
        return Single.fromCallable {
            tours.map { tour ->
                Hotel(
                    tour.id ?: "",
                    tour.city ?: "",
                    tour.name ?: "",
                    tour.place ?: "",
                    tour.price ?: "",
                    tour.categoryId ?: "",
                    tour.airportDistance ?: "",
                    tour.beachDistance ?: "",
                    tour.rating ?: "",
                    tour.parking ?: "",
                    tour.stars ?: "",
                    tour.foodSystem ?: "",
                    tour.foodType ?: "",
                    tour.photo1 ?: "",
                    tour.photo2 ?: "",
                    tour.photo3 ?: ""
                    // todo добавить поля
                )

            }
            return@fromCallable tours
        }
    }

//    override fun getUsersIfOffline(): Single<List<User>> {
//
//        return Single.fromCallable {
//            db.userDao.getAll().map { roomUser ->
//                User(
//                    roomUser.id, roomUser.firstName, roomUser.lastName,
//                    roomUser.middleName, roomUser.birthdate, roomUser.login, roomUser.email, roomUser.password,
//                )
//            }
//        }
//    }
}