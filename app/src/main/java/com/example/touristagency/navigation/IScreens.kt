package com.example.touristagency.navigation

import com.example.touristagency.mvp.model.tours.Tour
import com.github.terrakok.cicerone.Screen

interface IScreens {
    fun mainAllTours(): Screen
    fun hotTours(hotTours: MutableList<Tour>): Screen
    fun favourites(favouriteTours: MutableList<Tour>): Screen
    fun profile(favouriteTours: MutableList<Tour>): Screen
    fun tour(tour: Tour): Screen
//    fun repository(repository: GithubUserProfile): Screen
}