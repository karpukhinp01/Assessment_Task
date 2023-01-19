package com.example.nousassesment.network

import com.example.nousassesment.data.ItemList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://cloud.nousdigital.net/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NousApiService {
    @GET("s/Njedq4WpjWz4KKk/download")
    suspend fun getItems(): ItemList
}

object NousApi {
    val retrofitService: NousApiService by lazy {
        retrofit.create(NousApiService::class.java)
    }
}