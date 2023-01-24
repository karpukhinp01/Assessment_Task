package com.example.nousassesment.repositories

import com.example.nousassesment.data.ItemList
import retrofit2.http.GET

interface MainApi {
    @GET("s/Njedq4WpjWz4KKk/download")
    suspend fun getItems(): ItemList
}