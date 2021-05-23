package com.antoniomy82.photogallery.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentAddPhotoBinding
import com.antoniomy82.photogallery.databinding.FragmentBaseBinding
import com.antoniomy82.photogallery.model.Photo
import com.antoniomy82.photogallery.model.network.NetworkRepository
import com.antoniomy82.photogallery.ui.PhotosListAdapter
import com.antoniomy82.photogallery.utils.CommonUtil
import java.lang.ref.WeakReference
import kotlin.system.exitProcess

class GalleryViewModel : ViewModel() {

    //Base Fragment parameters
    private var frgBaseActivity: WeakReference<Activity>? = null
    private var frgBaseView: WeakReference<View>? = null
    private var mainBundle: Bundle? = null
    private var fragmentBaseBinding: FragmentBaseBinding? = null
    private var frgBaseContext: WeakReference<Context>? = null


    //Base Fragment parameters
    private var frgAddPhotoActivity: WeakReference<Activity>? = null
    private var frgAddPhotoView: WeakReference<View>? = null
    var fragmentAddPhotoBinding:FragmentAddPhotoBinding ?= null
    private var frgAddPhotoContext: WeakReference<Context>? = null

    //Live data parameters
    val retrieveLocalDdPhotos = MutableLiveData<List<Photo>>()
    val retrieveNetworkPhotos = MutableLiveData<List<Photo>>()


    //Recycler view variables
    private var recyclerView: WeakReference<RecyclerView>? = null
    private var actualPhotoList: MutableList<Photo>? = null

    //Local variables
    private var mMenu: PopupMenu? = null
    var selectedImagePreview : WeakReference<Bitmap> ?=null

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

    //Set Base fragment parameters in this VM
    fun setAddPhotoFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        fragmentAddPhotoBinding:FragmentAddPhotoBinding
    ) {
        this.frgAddPhotoActivity = WeakReference(frgActivity)
        this.frgAddPhotoContext = WeakReference(frgContext)
        this.frgAddPhotoView = WeakReference(frgView)
        this.fragmentAddPhotoBinding = fragmentAddPhotoBinding

        fragmentAddPhotoBinding.selectedPhoto.setImageBitmap(selectedImagePreview?.get())
    }

    fun setUI() {

        fragmentBaseBinding?.progressBar?.visibility = View.VISIBLE //Load ProgressBar

        //Call network repository
        frgBaseContext?.get()?.let {
            retrieveNetworkPhotos.let { it1 ->
                NetworkRepository().getAllPhoto(
                    it,
                    it1
                )
            }
        }

        setPhotoMenu()
        CommonUtil.galleryViewModel=this

    }

    //Set Photos List in RecyclerView
    fun setPhotosRecyclerViewAdapter(movieList: List<Photo>) {

        actualPhotoList = movieList.toMutableList()

        recyclerView =
            WeakReference(frgBaseView?.get()?.findViewById(R.id.rvPhotos) as RecyclerView)
        val manager: RecyclerView.LayoutManager =
            GridLayoutManager(frgBaseActivity?.get(), 2) //Orientation
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


    fun homeGalleryBackArrow() {
        frgBaseActivity?.get()?.finish()
        exitProcess(0)
    }

    private fun setPhotoMenu() {
        mMenu = fragmentBaseBinding?.headerAdd?.let {
            frgBaseContext?.get()?.let { it1 ->
                PopupMenu(
                    it1, it
                )
            }
        }

        mMenu?.menuInflater?.inflate(R.menu.photo, mMenu?.menu)

        mMenu?.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.photo_gallery -> {

                    val selectPic = Intent()
                    selectPic.type = "image/*"
                    selectPic.action = Intent.ACTION_GET_CONTENT
                    frgBaseActivity?.get()?.startActivityForResult(Intent.createChooser(selectPic, "Select Picture"), 200)

                }
                R.id.photo_camera -> {

                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    if (frgBaseActivity?.get()?.packageManager?.let(takePictureIntent::resolveActivity) != null) {
                        frgBaseActivity?.get()?.startActivityForResult(
                            takePictureIntent, 100)
                    }


                }
            }

            false
        }
    }

    fun showPhotoMenuButton() {
        mMenu?.show()
    }


}