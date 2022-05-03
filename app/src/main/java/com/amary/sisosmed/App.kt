package com.amary.sisosmed

import android.app.Application
import com.amary.sisosmed.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    networkModule,
                    sessionModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}