package com.example.touristagency.navigation

import com.example.touristagency.mvp.model.tours.Tour
import com.github.terrakok.cicerone.Screen

interface IScreens {
    fun mainAllTours(): Screen
    fun hotTours(): Screen
    fun favourites(): Screen
    fun profile(): Screen
    fun tour(tour: Tour): Screen
//    fun repository(repository: GithubUserProfile): Screen
}