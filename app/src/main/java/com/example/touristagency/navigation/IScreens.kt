package com.example.touristagency.navigation

import com.example.touristagency.mvp.model.hotels.Hotel
import com.github.terrakok.cicerone.Screen

interface IScreens {
    fun mainAllHotels(): Screen
    fun hotHotels(hotHotels: MutableList<Hotel>): Screen
    fun favourites(favouriteHotels: MutableList<Hotel>): Screen
    fun profile(favouriteHotels: MutableList<Hotel>): Screen
    fun hotel(hotel: Hotel): Screen
//    fun repository(repository: GithubUserProfile): Screen
}