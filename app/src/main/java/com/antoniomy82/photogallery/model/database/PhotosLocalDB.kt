package com.antoniomy82.photogallery.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.antoniomy82.photogallery.model.Photo

@Database(
    entities = [Photo::class],
    version = 1
)

abstract  class PhotosLocalDB: RoomDatabase() {

    abstract fun photosLocalDAO() : PhotosLocalDAO

    companion object{

        private var photosINSTANCE : PhotosLocalDB?=null

        fun getDatabaseClient(context: Context): PhotosLocalDB? {


            if(photosINSTANCE !=null) return photosINSTANCE

            synchronized(PhotosLocalDB::class.java){
                photosINSTANCE =
                    Room.databaseBuilder(context, PhotosLocalDB::class.java, "PhotosLocalDB").fallbackToDestructiveMigration().build()
                return photosINSTANCE
            }
        }
    }
}