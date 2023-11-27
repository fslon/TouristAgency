package com.example.touristagency.mvp.view.list

interface UserItemView : IItemView {
    fun setLogin(text: String) // todo сделать другие методы
    fun loadAvatar(url: String)

}