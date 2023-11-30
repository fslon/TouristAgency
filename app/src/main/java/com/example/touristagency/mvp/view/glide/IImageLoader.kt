package com.example.touristagency.mvp.view.glide

interface IImageLoader<T> {
    fun loadInto(url: String, container: T)
}
