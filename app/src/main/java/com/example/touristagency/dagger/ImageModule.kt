package com.example.touristagency.dagger

import android.widget.ImageView
import com.example.touristagency.mvp.view.glide.GlideImageLoader
import com.example.touristagency.mvp.view.glide.IImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ImageModule {
    @Singleton
    @Provides
    fun imageLoader(): IImageLoader<ImageView> = GlideImageLoader()


}