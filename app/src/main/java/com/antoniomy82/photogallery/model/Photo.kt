package com.antoniomy82.photogallery.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Photo")
data class Photo(
    @ColumnInfo(name = "ALBUM_ID") var albumId: Int? = null,
    @PrimaryKey @ColumnInfo(name = "ID") var id: Int,
    @ColumnInfo(name = "TITLE") var title: String? = null,
    @ColumnInfo(name = "URL") var url: String? = null,
    @ColumnInfo(name = "THUMBNAIL_URL") var thumbnailUrl: String? = null
)
