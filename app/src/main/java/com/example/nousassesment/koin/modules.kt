package com.example.nousassesment.koin

import com.example.nousassesment.repositories.MainApi
import com.example.nousassesment.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<MainApi> { MainApiImpl() }
    viewModel { MainViewModel(get()) }
}