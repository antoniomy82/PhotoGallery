package com.antoniomy82.photogallery.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentBaseBinding
import com.antoniomy82.photogallery.utils.CommonUtil
import com.antoniomy82.photogallery.utils.ResizePicture
import com.antoniomy82.photogallery.viewmodel.GalleryViewModel
import java.io.IOException


class BaseFragment : Fragment() {

    private var fragmentBaseBinding: FragmentBaseBinding? = null
    private var galleryViewModel: GalleryViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false)

        return fragmentBaseBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryViewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        fragmentBaseBinding?.galleryVM = galleryViewModel


        //Set base fragment parameters in this VM
        activity?.let {
            context?.let { it1 ->
                fragmentBaseBinding?.let { it2 ->
                    galleryViewModel?.setBaseFragmentBinding(
                        it,
                        it1, view, savedInstanceState, it2
                    )
                }
            }
        }

        galleryViewModel?.setUI()


        //Set observer to load recyclerview
        galleryViewModel?.retrieveNetworkPhotos?.observe(viewLifecycleOwner) {
            if (it != null) galleryViewModel?.setPhotosRecyclerViewAdapter(it)
            fragmentBaseBinding?.progressBar?.visibility = View.GONE
        }

    }

}