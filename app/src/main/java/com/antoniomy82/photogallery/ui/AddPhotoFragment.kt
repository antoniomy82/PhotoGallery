package com.antoniomy82.photogallery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentAddPhotoBinding
import com.antoniomy82.photogallery.utils.CommonUtil

class AddPhotoFragment : Fragment() {

    private var fragmentAddPhotoBinding: FragmentAddPhotoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddPhotoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_photo, container, false)

        return fragmentAddPhotoBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentAddPhotoBinding?.galleryVM=CommonUtil.galleryViewModel

        activity?.let { context?.let { it1 ->
            fragmentAddPhotoBinding?.let { it2 ->
                CommonUtil.galleryViewModel?.setAddPhotoFragmentBinding(it,
                    it1,view, it2
                )
            }
        } }
    }

}