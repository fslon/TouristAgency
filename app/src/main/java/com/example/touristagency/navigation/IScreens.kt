package com.example.touristagency.navigation

import com.github.terrakok.cicerone.Screen

interface IScreens {
    fun mainAllTours(): Screen
    fun hotTours(): Screen
    fun favourites(): Screen
    fun profile(): Screen
//    fun repository(repository: GithubUserProfile): Screen
}