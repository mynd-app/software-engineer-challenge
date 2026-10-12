package com.example.myndchallenge.android

import android.app.Application
import com.example.myndchallenge.data.api.ApiConfig
import com.example.myndchallenge.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChallengeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChallengeApplication)
            modules(
                sharedModule(
                    ApiConfig(
                        baseUrl = BuildConfig.API_BASE_URL,
                        token = BuildConfig.API_TOKEN,
                    ),
                ),
            )
        }
    }
}
