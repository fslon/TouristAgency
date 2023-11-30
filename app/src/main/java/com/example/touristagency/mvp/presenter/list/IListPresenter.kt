package com.example.touristagency.mvp.presenter.list

import com.example.touristagency.mvp.view.list.IItemView

interface IListPresenter<V : IItemView> {
    var itemClickListener: ((V) -> Unit)?
    var favouriteButtonClickListener: ((V) -> Unit)?
    fun bindView(view: V)
    fun getCount(): Int
}