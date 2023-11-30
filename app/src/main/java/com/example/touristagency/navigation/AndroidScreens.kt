package com.example.touristagency.navigation

import com.example.touristagency.ui.fragments.FavouritesFragment
import com.example.touristagency.ui.fragments.HotToursFragment
import com.example.touristagency.ui.fragments.ProfileFragment
import com.example.touristagency.ui.fragments.TourFragment
import com.example.touristagency.ui.fragments.ToursMainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AndroidScreens : IScreens {
    override fun mainAllTours() = FragmentScreen { ToursMainFragment.newInstance() }
    override fun hotTours() = FragmentScreen { HotToursFragment.newInstance() }
    override fun favourites() = FragmentScreen { FavouritesFragment.newInstance() }
    override fun profile() = FragmentScreen { ProfileFragment.newInstance() }
    override fun tour() = FragmentScreen { TourFragment.newInstance() }

//    override fun profileUser(user: GithubUser) = FragmentScreen { ProfileFragment.newInstance(user) }
//    override fun repository(repository: GithubUserProfile) = FragmentScreen { RepositoryFragment.newInstance(repository) }
}