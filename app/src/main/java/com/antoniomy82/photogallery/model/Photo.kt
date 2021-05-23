package com.antoniomy82.photogallery.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Photo")
data class Photo(
    @ColumnInfo(name = "ALBUM_ID") var albumId: Int? = null,
    @PrimaryKey @ColumnInfo(name = "ID") var id: Int,
    @ColumnInfo(name = "TITLE") var title: String? = null,
    @ColumnInfo(name = "URL") var url: String? = null,
    @ColumnInfo(name = "THUMBNAIL_URL") var thumbnailUrl: String? = null,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var imageBmp: ByteArray? =null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (imageBmp != null) {
            if (other.imageBmp == null) return false
            if (!imageBmp.contentEquals(other.imageBmp)) return false
        } else if (other.imageBmp != null) return false

        return true
    }

    override fun hashCode(): Int {
        return imageBmp?.contentHashCode() ?: 0
    }
}