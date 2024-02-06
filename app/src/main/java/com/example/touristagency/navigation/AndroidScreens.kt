package com.example.touristagency.navigation

import com.example.touristagency.mvp.model.hotels.Hotel
import com.example.touristagency.ui.fragments.FavouritesFragment
import com.example.touristagency.ui.fragments.HotHotelsFragment
import com.example.touristagency.ui.fragments.ProfileFragment
import com.example.touristagency.ui.fragments.HotelFragment
import com.example.touristagency.ui.fragments.HotelsMainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AndroidScreens : IScreens {
    override fun mainAllHotels() = FragmentScreen { HotelsMainFragment.newInstance() }
    override fun hotHotels(hotHotels: MutableList<Hotel>) = FragmentScreen { HotHotelsFragment.newInstance(hotHotels) }
    override fun favourites(favouriteHotels: MutableList<Hotel>) = FragmentScreen { FavouritesFragment.newInstance(favouriteHotels) }
    override fun profile(favouriteHotels: MutableList<Hotel>) = FragmentScreen { ProfileFragment.newInstance() }
    override fun hotel(hotel: Hotel) = FragmentScreen { HotelFragment.newInstance(hotel) }

//    override fun profileUser(user: GithubUser) = FragmentScreen { ProfileFragment.newInstance(user) }
//    override fun repository(repository: GithubUserProfile) = FragmentScreen { RepositoryFragment.newInstance(repository) }
}