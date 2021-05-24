package com.antoniomy82.photogallery.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentAddPhotoBinding
import com.antoniomy82.photogallery.databinding.FragmentBaseBinding
import com.antoniomy82.photogallery.model.Photo
import com.antoniomy82.photogallery.model.database.LocalDbRepository
import com.antoniomy82.photogallery.model.network.NetworkRepository
import com.antoniomy82.photogallery.ui.AddPhotoFragment
import com.antoniomy82.photogallery.ui.BaseFragment
import com.antoniomy82.photogallery.ui.PhotosListAdapter
import com.antoniomy82.photogallery.utils.CommonUtil
import com.antoniomy82.photogallery.utils.ResizePicture
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference
import kotlin.system.exitProcess

class GalleryViewModel : ViewModel() {

    //Base Fragment parameters
    private var frgBaseActivity: WeakReference<Activity>? = null
    private var frgBaseView: WeakReference<View>? = null
    private var mainBundle: Bundle? = null
    private var fragmentBaseBinding: FragmentBaseBinding? = null
    private var startForResult: ActivityResultLauncher<Intent>? = null
    var frgBaseContext: WeakReference<Context>? = null


    //Add Photo Fragment parameters
    private var frgAddPhotoActivity: WeakReference<Activity>? = null
    private var frgAddPhotoView: WeakReference<View>? = null
    private var fragmentAddPhotoBinding: FragmentAddPhotoBinding? = null
    private var frgAddPhotoContext: WeakReference<Context>? = null

    //Live data parameters
    val retrievePhotos = MutableLiveData<List<Photo>>()
    val retrieveLocalPhotos = MutableLiveData<List<Photo>>()


    //Recycler view variables
    private var recyclerView: WeakReference<RecyclerView>? = null
    private var actualPhotoList: MutableList<Photo>? = null
    private var retrieveNetwork: List<Photo>? = null
    private var retrieveLocal: List<Photo>? = null
    private var listSize = 0

    //Local variables
    private var mMenu: PopupMenu? = null
    private var selectedImagePreview: WeakReference<Bitmap>? = null
    var editingPhoto: Photo? = null

    //Set Base fragment parameters in this VM
    fun setBaseFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        mainBundle: Bundle?,
        fragmentBaseBinding: FragmentBaseBinding,
        startForResult: ActivityResultLauncher<Intent>
    ) {
        this.frgBaseActivity = WeakReference(frgActivity)
        this.frgBaseContext = WeakReference(frgContext)
        this.frgBaseView = WeakReference(frgView)
        this.mainBundle = mainBundle
        this.fragmentBaseBinding = fragmentBaseBinding
        this.startForResult = startForResult
    }

    //Set AddPhoto fragment parameters in this VM
    fun setAddPhotoFragmentBinding(
        frgActivity: Activity,
        frgContext: Context,
        frgView: View,
        fragmentAddPhotoBinding: FragmentAddPhotoBinding
    ) {
        this.frgAddPhotoActivity = WeakReference(frgActivity)
        this.frgAddPhotoContext = WeakReference(frgContext)
        this.frgAddPhotoView = WeakReference(frgView)
        this.fragmentAddPhotoBinding = fragmentAddPhotoBinding

        //load image
        if (selectedImagePreview?.get() != null) fragmentAddPhotoBinding.selectedPhoto.setImageBitmap(
            selectedImagePreview?.get()
        )
    }


    fun setUI() {

        fragmentBaseBinding?.progressBar?.visibility = View.VISIBLE //Load ProgressBar

        LocalDbRepository().getAllPhotos(
            frgBaseContext?.get(),
            retrieveLocalPhotos
        ) //Load local repository

        if (frgBaseContext?.get()?.let { CommonUtil.isOnline(it) } == true) {
            //Call network repository
            frgBaseContext?.get()?.let {
                retrievePhotos.let { it1 ->
                    NetworkRepository().getAllPhoto(
                        it,
                        it1
                    )
                }
            }
        } else LocalDbRepository().getAllPhotos(frgBaseContext?.get(), retrievePhotos)

        setPhotoMenu()

    }


    fun checkRepositories(
        retrieveLocalObserver: List<Photo>? = null,
        retrieveNetworkObserver: List<Photo>? = null
    ) {

        retrieveLocalObserver.let { this.retrieveLocal = it }
        retrieveNetworkObserver.let { this.retrieveNetwork = it }

        Log.d(
            "CheckRepositories",
            "-->In _Local: " + retrieveLocal?.size + " _Network:" + retrieveNetwork?.size
        )
        when {
            retrieveLocal?.size ?: 0 > retrieveNetwork?.size ?: 0 -> {
                retrieveLocal?.let {
                    setPhotosRecyclerViewAdapter(
                        it
                    )
                }
                Log.d("Case 1", "retrieveLocal")
            }
            retrieveLocal?.size ?: 0 < retrieveNetwork?.size ?: 0 -> {
                retrieveNetwork?.let { setPhotosRecyclerViewAdapter(it) }
                Log.d("Case 2", "retrieveNetwork")
            }

        }
    }

    //Set Photos List in RecyclerView
    private fun setPhotosRecyclerViewAdapter(photosList: List<Photo>) {

        actualPhotoList = (photosList.reversed()).toMutableList()
        listSize = actualPhotoList?.size ?: 0

        saveInLocalDB()

        recyclerView =
            WeakReference(frgBaseView?.get()?.findViewById(R.id.rvPhotos) as RecyclerView)
        val manager: RecyclerView.LayoutManager =
            GridLayoutManager(frgBaseActivity?.get(), 2) //Orientation
        recyclerView?.get()?.layoutManager = manager


        recyclerView?.get()?.adapter = frgBaseContext?.get()?.let {
            actualPhotoList?.let { it2 ->
                PhotosListAdapter(
                    this, it2,
                    it
                )
            }
        }

        CommonUtil.galleryViewModel = this
        fragmentBaseBinding?.galleryVM = this
        recyclerView?.get()?.adapter?.notifyDataSetChanged()
    }


    fun homeGalleryBackArrow() {
        frgBaseActivity?.get()?.finish()
        exitProcess(0)
    }

    fun addPhotoBackArrow() {
        (frgAddPhotoContext?.get() as AppCompatActivity).supportFragmentManager.let {
            CommonUtil.replaceFragment(
                BaseFragment(),
                it
            )
        }
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
            editingPhoto = null

            when (menuItem.itemId) {
                R.id.photo_gallery -> {

                    val selectedPhoto = Intent()
                    selectedPhoto.type = "image/*"
                    selectedPhoto.action = Intent.ACTION_GET_CONTENT

                    CommonUtil.requestCode = 200
                    CommonUtil.galleryViewModel = this
                    startForResult?.launch(selectedPhoto)
                }

                R.id.photo_camera -> {

                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    if (frgBaseActivity?.get()?.packageManager?.let(takePictureIntent::resolveActivity) != null) {
                        CommonUtil.requestCode = 100
                        CommonUtil.galleryViewModel = this
                        startForResult?.launch(takePictureIntent)
                    }
                }
            }

            false
        }
    }

    fun showPhotoMenuButton() {
        mMenu?.show()
    }

    fun takePhotoForResult(requestCode: Int, data: Intent?) {
        when (requestCode) {
            100 -> {
                val extras = data?.extras
                val imageBitmap = extras?.get("data") as Bitmap?

                CommonUtil.galleryViewModel?.selectedImagePreview = WeakReference(imageBitmap)

                (frgBaseContext?.get() as AppCompatActivity).supportFragmentManager.let {
                    CommonUtil.replaceFragment(
                        AddPhotoFragment(),
                        it
                    )
                }

            }

            200 -> {
                val selectedImageUri = data?.data

                val mBitmap = selectedImageUri?.let {
                    frgBaseActivity?.get()?.contentResolver?.let { it1 ->
                        ResizePicture(
                            it,
                            it1
                        ).bitmap
                    }
                }
                CommonUtil.galleryViewModel?.selectedImagePreview = WeakReference(mBitmap)

                CommonUtil.replaceFragment(
                    AddPhotoFragment(),
                    (frgBaseContext?.get() as AppCompatActivity).supportFragmentManager
                )
            }
        }
    }

    fun savePhotoButton() {
        frgAddPhotoContext?.get()?.let {
            frgAddPhotoView?.get()?.let { it1 ->
                CommonUtil.hideKeyboard(
                    it,
                    it1
                )
            }
        }


        val mByteArray = ByteArrayOutputStream()
        selectedImagePreview?.get()?.compress(Bitmap.CompressFormat.JPEG, 100, mByteArray)
        Log.d("saveButtonState", listSize.toString())

        var mId = listSize + 1

        if (editingPhoto != null) editingPhoto?.id?.let { mId = it }

        val mPhoto = Photo(
            albumId = 1,
            id = mId,
            title = fragmentAddPhotoBinding?.addTittle?.text.toString(),
            url = null,
            thumbnailUrl = null,
            imageBmp = mByteArray.toByteArray()
        )

        LocalDbRepository().insertPhoto(frgAddPhotoContext?.get(), mPhoto)

        CommonUtil.galleryViewModel = this

        addPhotoBackArrow()
    }

    fun deletePhotoButton(mPhoto: Photo, position: Int) {

        LocalDbRepository().deletePhoto(frgAddPhotoContext?.get(), mPhoto.id)

        CommonUtil.galleryViewModel = this

        //Refresh recyclerView
        actualPhotoList?.removeAt(position)
        listSize--
        recyclerView?.get()?.adapter?.notifyItemRemoved(position)
        recyclerView?.get()?.adapter?.notifyDataSetChanged()
        saveInLocalDB()
    }

    fun editPhoto() {

        if (editingPhoto != null) {

            fragmentAddPhotoBinding?.btnModify?.visibility = View.VISIBLE
            fragmentAddPhotoBinding?.btnSave?.visibility = View.GONE
            fragmentAddPhotoBinding?.headerAddPhotoTitle?.setText(R.string.title_edit_photo)
            fragmentAddPhotoBinding?.addTittle?.setText(editingPhoto?.title)

            if (editingPhoto?.thumbnailUrl?.isNotEmpty() == true) {
                Picasso.get().load(editingPhoto?.thumbnailUrl)
                    .placeholder(R.mipmap.ic_no_image)
                    .resize(150, 150)
                    .into(fragmentAddPhotoBinding?.selectedPhoto)
            }


            //Set image from byteArray
            if (editingPhoto?.imageBmp != null) {
                selectedImagePreview = WeakReference(
                    BitmapFactory.decodeByteArray(
                        editingPhoto?.imageBmp,
                        0,
                        (editingPhoto?.imageBmp)?.size ?: 0
                    )
                )
                fragmentAddPhotoBinding?.selectedPhoto?.setImageBitmap(selectedImagePreview?.get())
            }
        }
    }

    private fun saveInLocalDB() {

        CoroutineScope(IO).launch {
            val mSize = actualPhotoList?.size ?: 0
            for (i in 0 until mSize) {
                LocalDbRepository().insertPhoto(frgBaseContext?.get(), actualPhotoList?.get(i))
                if (i == mSize - 1) Log.d("__localBD", "photos saved")
            }

        }
    }
}