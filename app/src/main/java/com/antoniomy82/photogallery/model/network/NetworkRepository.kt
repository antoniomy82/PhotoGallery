package com.antoniomy82.photogallery.model.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.model.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkRepository {

    fun getAllPhoto(
        context: Context? = null,
        mGallery: MutableLiveData<List<Photo>>? = null
    ): Int {
        var mockTest = 0

        if (context != null && mGallery != null) {
            val call: Call<List<Photo>>? = ApiAdapter().api?.getAllPhotos()
            val results = mutableListOf<Photo>()

            call?.enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {


                    if (response.isSuccessful) {

                        val responseObtained = response.body()

                        val resultSize = responseObtained?.size ?: 0

                        for (i in 0 until resultSize) {

                            responseObtained?.get(i)?.id?.let {
                                Photo(
                                    responseObtained[i].albumId,
                                    it,
                                    responseObtained[i].title,
                                    responseObtained[i].url,
                                    responseObtained[i].thumbnailUrl,

                                    )
                            }

                            responseObtained?.get(i)?.id?.let {
                                Photo(
                                    responseObtained[i].albumId,
                                    it,
                                    responseObtained[i].title,
                                    responseObtained[i].url,
                                    responseObtained[i].thumbnailUrl,
                                )
                            }?.let {
                                results.add(
                                    it
                                )
                            }


                            Log.d(
                                "__retrieveList ",
                                i.toString() + " - " + responseObtained?.get(i)?.id + " tittle " + responseObtained?.get(
                                    i
                                )?.title
                            )
                        }

                        mGallery.value = results
                    }
                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                    Log.e("__error", t.toString())
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_api_services),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else mockTest = 5000

        return mockTest
    }

}