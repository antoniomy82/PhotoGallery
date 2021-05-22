package com.antoniomy82.photogallery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentBaseBinding
import com.antoniomy82.photogallery.model.Photo
import com.antoniomy82.photogallery.model.network.NetworkRepository
import com.antoniomy82.photogallery.viewmodel.GalleryViewModel


class BaseFragment : Fragment() {

    var fragmentBaseBinding: FragmentBaseBinding? = null
    var galleryViewModel: GalleryViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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



        context?.let { galleryViewModel?.retrieveNetworkPhotos?.let { it1 ->
            NetworkRepository().getAllPhoto(it,
                it1
            )
        } }

        galleryViewModel?.retrieveNetworkPhotos?.observe(viewLifecycleOwner){
            if(it!=null) galleryViewModel?.setPhotosRecyclerViewAdapter(it)
        }
    }
}