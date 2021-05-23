package com.antoniomy82.photogallery.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.utils.CommonUtil
import com.antoniomy82.photogallery.utils.CommonUtil.Companion.galleryViewModel
import com.antoniomy82.photogallery.utils.ResizePicture
import java.io.IOException
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load base fragment
        CommonUtil.replaceFragment(BaseFragment(), supportFragmentManager)
    }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            resultCode == Activity.RESULT_OK && requestCode == 100 -> {
                val extras = data?.extras
                val imageBitmap = extras?.get("data") as Bitmap?

                galleryViewModel?.selectedImagePreview = WeakReference(imageBitmap)
                CommonUtil.replaceFragment(
                    AddPhotoFragment(),
                    (this as AppCompatActivity).supportFragmentManager
                )
            }

            resultCode == Activity.RESULT_OK && requestCode == 200 -> {
                val selectedImageUri = data?.data

                try {
                    val mBitmap = selectedImageUri?.let {
                        contentResolver?.let { it1 ->
                            ResizePicture(
                                it,
                                it1
                            ).bitmap
                        }
                    }
                    galleryViewModel?.selectedImagePreview = WeakReference(mBitmap)

                    CommonUtil.replaceFragment(
                        AddPhotoFragment(),
                        (this as AppCompatActivity).supportFragmentManager
                    )

                } catch (e: IOException) {
                    Log.e(MainActivity::class.java.simpleName, "Error load image ", e)
                }
            }
        }
    }
}