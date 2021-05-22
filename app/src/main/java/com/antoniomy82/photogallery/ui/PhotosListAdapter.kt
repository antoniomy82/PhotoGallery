package com.antoniomy82.photogallery.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.AdapterPhotosListBinding
import com.antoniomy82.photogallery.model.Photo
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.adapterPhotosListBinding.galleryVM = galleryVM
        holder.adapterPhotosListBinding.photo = photosList[position]

        //Set image
        if (photosList[position].thumbnailUrl?.isNotEmpty() == true) {
            Picasso.get().load(photosList[position].thumbnailUrl)
                .placeholder(R.mipmap.ic_no_image)
                .resize(150, 150)
                .into(holder.adapterPhotosListBinding.imagePhoto)
        }


        holder.adapterPhotosListBinding.photoNumber.text=context.getString(R.string.photo_number)+position

        //Set background color so that different cells are noticeable
        if (position % 2 == 0) holder.adapterPhotosListBinding.root.setBackgroundColor(
            Color.parseColor(
                "#96b5ce"
            )
        )
        else holder.adapterPhotosListBinding.root.setBackgroundColor(Color.parseColor("#dce6ee"))


        //on Click item - open URL
        holder.adapterPhotosListBinding.root.setOnClickListener {

            /*
            RecipesUtils.replaceFragment(moviesList[position].href?.let { it1 ->
                ShowHrefFragment(
                    it1,
                    moviesVM
                )
            }, (context as AppCompatActivity).supportFragmentManager) */
        }


    }


    override fun getItemCount(): Int {
        return photosList.size
    }

    class ViewHolder(val adapterPhotosListBinding: AdapterPhotosListBinding) :
        RecyclerView.ViewHolder(adapterPhotosListBinding.root)

}
