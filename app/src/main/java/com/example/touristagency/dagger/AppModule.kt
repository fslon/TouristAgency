package com.example.touristagency.dagger

import com.example.touristagency.App
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler

@Module
class AppModule(val app: App) {
    @Provides
    fun app(): App {
        return app
    }

    @Provides
    fun mainScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
