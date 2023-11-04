package com.example.touristagency.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ToursView : MvpView {
    fun init()
    fun updateList()
    fun release()
}