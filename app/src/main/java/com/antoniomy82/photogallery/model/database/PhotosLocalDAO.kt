package com.antoniomy82.photogallery.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.antoniomy82.photogallery.model.Photo


@Dao
interface PhotosLocalDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(movie: Photo)

    @Query("DELETE FROM Photo WHERE id =:id")
    fun deletePhoto(id: Int)

    @Query("SELECT * FROM Photo")
    fun getAllPhotos(): List<Photo>

}