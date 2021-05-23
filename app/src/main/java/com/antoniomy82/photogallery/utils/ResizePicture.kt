package com.antoniomy82.photogallery.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.support.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InvalidObjectException

@Suppress("DEPRECATION")
class ResizePicture(private var uri: Uri, private var resolver: ContentResolver) {
    private var path: String? = null
    private var orientation: Matrix? = null
    private var storedHeight = 1024
    private var storedWidth = 768

    companion object {
        var MAX_WIDTH = 768
        var MAX_HEIGHT = 1024
    }

    @get:Throws(IOException::class)
    private val information: Boolean
        get() {
            if (informationFromMediaDatabase) return true
                        return informationFromFileSystem
                    }

    /* Support for gallery apps and remote ("picasso") images */
    private val informationFromMediaDatabase: Boolean

        @RequiresApi(Build.VERSION_CODES.Q)
        get() {
                    val fields = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION)
                    val cursor = resolver.query(uri, fields, null, null, null) ?: return false
                    cursor.moveToFirst()
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    val orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION))
                    this.orientation = Matrix()
                    this.orientation?.setRotate(orientation.toFloat())
                    cursor.close()
                    return true
            }

    /* Support for file managers and dropbox */


    @get:Throws(IOException::class)
    private val informationFromFileSystem: Boolean
        get() {
            path = uri.path
            if (path == null) return false
            val exif = ExifInterface(path!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            this.orientation = Matrix()

            when (orientation) {
                    ExifInterface.ORIENTATION_NORMAL -> { }
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> this.orientation?.setScale(-1f, 1f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> this.orientation?.setRotate(180f)
                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> this.orientation?.setScale(1f, -1f)
                    ExifInterface.ORIENTATION_TRANSPOSE -> {
                        this.orientation?.setRotate(90f)
                        this.orientation?.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_ROTATE_90 -> this.orientation?.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    this.orientation?.setRotate(-90f)
                    this.orientation?.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> this.orientation?.setRotate(-90f)
            }
            return true
        }

    /* The input stream could be reset instead of closed and reopened if it were possible
           to reliably wrap the input stream on a buffered stream, but it's not possible because
           decodeStream() places an upper read limit of 1024 bytes for a reset to be made (it calls
           mark(1024) on the stream). */
    @get:Throws(IOException::class)
    private val storedDimensions: Boolean
        get() {
            val input = resolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options)

            /* The input stream could be reset instead of closed and reopened if it were possible
               to reliably wrap the input stream on a buffered stream, but it's not possible because
               decodeStream() places an upper read limit of 1024 bytes for a reset to be made (it calls
               mark(1024) on the stream). */
            input?.close()

            if (options.outHeight <= 0 || options.outWidth <= 0) return false
            storedHeight = options.outHeight
            storedWidth = options.outWidth
            return true
        }

    @get:Throws(IOException::class)
    val bitmap: Bitmap?
        get() {
                if (!information) throw FileNotFoundException()
                if (!storedDimensions) throw InvalidObjectException(null)
                val rect = RectF(0F, 0F, storedWidth.toFloat(), storedHeight.toFloat())
                orientation!!.mapRect(rect)

                var width = rect.width().toInt()
                var height = rect.height().toInt()
                var subSample = 1

                while (width > MAX_WIDTH || height > MAX_HEIGHT) {
                    width /= 2
                    height /= 2
                    subSample *= 2
            }

            if (width == 0 || height == 0) throw InvalidObjectException(null)
            val options = BitmapFactory.Options()
            options.inSampleSize = subSample

            val subSampled = BitmapFactory.decodeStream(resolver.openInputStream(uri), null, options)
            val picture: Bitmap?

            if (!orientation!!.isIdentity) {
                picture = Bitmap.createBitmap(subSampled!!, 0, 0, options.outWidth, options.outHeight, orientation, false)
                subSampled.recycle()
            } else picture = subSampled

            return picture
        }
}