package com.example.myndchallenge.di

import com.example.myndchallenge.data.api.ApiConfig
import com.example.myndchallenge.data.api.MyndApiClient
import com.example.myndchallenge.data.api.platformHttpEngine
import com.example.myndchallenge.data.repository.CheckInRepository
import com.example.myndchallenge.data.repository.ConfigRepository
import com.example.myndchallenge.presentation.checkin.CheckInViewModel
import com.example.myndchallenge.presentation.home.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun sharedModule(config: ApiConfig): Module =
    module {
        single { config }
        single { MyndApiClient(get(), platformHttpEngine()) }
        single { CheckInRepository(get()) }
        single { ConfigRepository(get()) }
        viewModelOf(::HomeViewModel)
        viewModelOf(::CheckInViewModel)
    }
