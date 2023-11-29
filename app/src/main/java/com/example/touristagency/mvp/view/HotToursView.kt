package com.example.touristagency.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface HotToursView : MvpView {
    fun init()
    fun updateList()
    fun release()

    fun initCurrentCurrency(currentCurrency: String)
    fun initBottomNavigationMenu()

    fun initSortingMenu(sortingStrings: List<String>)
    fun setTextSortingButton(text: String)
    fun initSortingButton()


    fun testInitFirstRecyclerItem()
    fun testInitSecondRecyclerItem()
    fun testInitThirdRecyclerItem()

}