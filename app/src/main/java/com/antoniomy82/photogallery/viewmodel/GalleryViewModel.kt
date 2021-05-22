package com.antoniomy82.photogallery.viewmodel

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentBaseBinding
import com.antoniomy82.photogallery.model.Photo
import com.antoniomy82.photogallery.ui.PhotosListAdapter
import java.lang.ref.WeakReference

class GalleryViewModel:ViewModel() {

    //Base Fragment parameters
    private var frgBaseActivity: WeakReference<Activity>? = null
    private var frgBaseView: WeakReference<View>? = null
    private var mainBundle: Bundle? = null
    private var fragmentBaseBinding: FragmentBaseBinding? = null
    var frgBaseContext: WeakReference<Context>? = null


    //Live data parameters
    val retrieveLocalDdPhotos = MutableLiveData<List<Photo>>()
    val retrieveNetworkPhotos = MutableLiveData<List<Photo>>()


    //Recycler view variables
    private var recyclerView: WeakReference<RecyclerView>? = null
    private var actualPhotoList: MutableList<Photo>? = null


    //Set Base fragment parameters in this VM
    fun setBaseFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        mainBundle: Bundle?,
        fragmentBaseBinding: FragmentBaseBinding
    ) {
        this.frgBaseActivity = WeakReference(frgActivity)
        this.frgBaseContext = WeakReference(frgContext)
        this.frgBaseView = WeakReference(frgView)
        this.mainBundle = mainBundle
        this.fragmentBaseBinding = fragmentBaseBinding
    }

    //Set Photos List in RecyclerView
    fun setPhotosRecyclerViewAdapter(movieList: List<Photo>) {

        actualPhotoList = movieList.toMutableList()

        recyclerView =
            WeakReference(frgBaseView?.get()?.findViewById(R.id.rvPhotos) as RecyclerView)
        val manager: RecyclerView.LayoutManager =
            GridLayoutManager(frgBaseActivity?.get(),2) //Orientation
        recyclerView?.get()?.layoutManager = manager
        movieList.sortedBy { it.title }

        recyclerView?.get()?.adapter = frgBaseContext?.get()?.let {
            actualPhotoList?.let { it2 ->
                PhotosListAdapter(
                    this, it2,
                    it
                )
            }

        }

        fragmentBaseBinding?.galleryVM = this
        recyclerView?.get()?.adapter?.notifyDataSetChanged()
    }

}