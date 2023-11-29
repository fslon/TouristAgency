package com.example.touristagency.dagger

import androidx.room.Room
import com.example.touristagency.App
import com.example.touristagency.mvp.model.room.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class CacheModule {
    @Singleton
    @Provides
    fun database(app: App): Database = Room.databaseBuilder(
        app,
        Database::class.java, Database.DB_NAME
    )
        .build()


}