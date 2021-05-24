package com.antoniomy82.photogallery.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.AdapterPhotosListBinding
import com.antoniomy82.photogallery.model.Photo
import com.antoniomy82.photogallery.utils.CommonUtil
import com.antoniomy82.photogallery.viewmodel.GalleryViewModel
import com.squareup.picasso.Picasso


class PhotosListAdapter(
    private val galleryVM: GalleryViewModel,
    private val photosList: List<Photo>,
    private val context: Context
) :
    RecyclerView.Adapter<PhotosListAdapter.ViewHolder>() {


    //Inflate view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_photos_list,
            parent,
            false
        )
    )


    //Binding each element with object element
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.adapterPhotosListBinding.galleryVM = galleryVM
        holder.adapterPhotosListBinding.photo = photosList[position]

        //Set image from url
        if (photosList[position].thumbnailUrl?.isNotEmpty() == true) {
            Picasso.get().load(photosList[position].thumbnailUrl)
                .placeholder(R.mipmap.ic_no_image)
                .resize(150, 150)
                .into(holder.adapterPhotosListBinding.imagePhoto)
        }

        //Set image from take photo
        if (photosList[position].imageBmp != null) holder.adapterPhotosListBinding.imagePhoto.setImageBitmap(
            BitmapFactory.decodeByteArray(
                photosList[position].imageBmp,
                0,
                (photosList[position].imageBmp)?.size ?: 0
            )
        )

        val setPosition =
            context.getString(R.string.photo_number) + photosList[position].id.toString()
        holder.adapterPhotosListBinding.photoNumber.text = setPosition

        //Set background color so that different cells are noticeable
        if (position % 2 == 0) holder.adapterPhotosListBinding.root.setBackgroundColor(
            Color.parseColor(
                "#96b5ce"
            )
        )
        else holder.adapterPhotosListBinding.root.setBackgroundColor(Color.parseColor("#dce6ee"))


        holder.adapterPhotosListBinding.deleteIcon.setOnClickListener {
            galleryVM.deletePhotoButton(photosList[position], position)
        }

        holder.adapterPhotosListBinding.editIcon.setOnClickListener {
            galleryVM.editingPhoto = photosList[position]

            (galleryVM.frgBaseContext?.get() as AppCompatActivity).supportFragmentManager.let {
                CommonUtil.replaceFragment(
                    AddPhotoFragment(),
                    it
                )
            }
        }

    }


    override fun getItemCount(): Int {
        return photosList.size
    }

    class ViewHolder(val adapterPhotosListBinding: AdapterPhotosListBinding) :
        RecyclerView.ViewHolder(adapterPhotosListBinding.root)

}
