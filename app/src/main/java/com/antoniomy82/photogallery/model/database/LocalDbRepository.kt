package com.antoniomy82.photogallery.model.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.antoniomy82.photogallery.model.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalDbRepository {

    //DB instance
    private var photosLocalDB: PhotosLocalDB? = null

    private fun initializeDB(context: Context): PhotosLocalDB? {
        return PhotosLocalDB.getDatabaseClient(context)
    }


    fun insertPhoto(context: Context? = null, photo: Photo? = null): Boolean {

        var mockTest = false

        if (context != null && photo != null) {
            photosLocalDB = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                photosLocalDB?.photosLocalDAO()?.insertPhoto(photo)
            }
        } else mockTest = true

        return mockTest

    }

    fun deletePhoto(context: Context? = null, id: Int? = null):Boolean {

        var mockTest = false

        if (context != null && id != null) {
            photosLocalDB = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    photosLocalDB?.photosLocalDAO()?.deletePhoto(id)
                } catch (e: Exception) {
                    Log.e("__deleteError", e.toString())
                }

            }
        } else mockTest = true

        return mockTest

    }

    fun getAllPhotos(context: Context? = null, photosList: MutableLiveData<List<Photo>>? = null):Int {
        var mockTest = 0

        if (photosList != null && context != null) {
            photosLocalDB = initializeDB(context)

            CoroutineScope(Dispatchers.IO).launch {
                photosList.postValue(photosLocalDB?.photosLocalDAO()?.getAllPhotos())
            }
        }else mockTest=5000

        return mockTest
    }

}