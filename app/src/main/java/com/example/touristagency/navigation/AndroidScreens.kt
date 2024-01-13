package com.example.touristagency.navigation

import com.example.touristagency.mvp.model.tours.Tour
import com.example.touristagency.ui.fragments.FavouritesFragment
import com.example.touristagency.ui.fragments.HotToursFragment
import com.example.touristagency.ui.fragments.ProfileFragment
import com.example.touristagency.ui.fragments.TourFragment
import com.example.touristagency.ui.fragments.ToursMainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AndroidScreens : IScreens {
    override fun mainAllTours() = FragmentScreen { ToursMainFragment.newInstance() }
    override fun hotTours(hotTours: MutableList<Tour>) = FragmentScreen { HotToursFragment.newInstance(hotTours) }
    override fun favourites(favouriteTours: MutableList<Tour>) = FragmentScreen { FavouritesFragment.newInstance(favouriteTours) }
    override fun profile(favouriteTours: MutableList<Tour>) = FragmentScreen { ProfileFragment.newInstance() }
    override fun tour(tour: Tour) = FragmentScreen { TourFragment.newInstance(tour) }

//    override fun profileUser(user: GithubUser) = FragmentScreen { ProfileFragment.newInstance(user) }
//    override fun repository(repository: GithubUserProfile) = FragmentScreen { RepositoryFragment.newInstance(repository) }
}