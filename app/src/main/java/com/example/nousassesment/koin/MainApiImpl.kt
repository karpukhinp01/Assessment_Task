package com.example.nousassesment.koin

import com.example.nousassesment.data.ItemList
import com.example.nousassesment.repositories.MainApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory



private const val BASE_URL =
    "https://cloud.nousdigital.net/"

class MainApiImpl: MainApi {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: MainApi =
            retrofit.create(MainApi::class.java)

    override suspend fun getItems(): ItemList {
        return retrofitService.getItems()
    }

}


