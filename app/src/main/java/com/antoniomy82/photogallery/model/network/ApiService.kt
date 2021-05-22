package com.antoniomy82.photogallery.model.network
import com.antoniomy82.photogallery.model.Photo
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("photos")
    fun getAllPhotos(): Call<List<Photo>>
}