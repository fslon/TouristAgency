package com.example.touristagency.navigation

import com.example.touristagency.ui.fragments.ToursMainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AndroidScreens : IScreens {
    override fun mainAllTours() = FragmentScreen { ToursMainFragment.newInstance() }
//    override fun profileUser(user: GithubUser) = FragmentScreen { ProfileFragment.newInstance(user) }
//    override fun repository(repository: GithubUserProfile) = FragmentScreen { RepositoryFragment.newInstance(repository) }
}