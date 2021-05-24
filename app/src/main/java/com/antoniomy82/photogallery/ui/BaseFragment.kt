package com.antoniomy82.photogallery.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.databinding.FragmentBaseBinding
import com.antoniomy82.photogallery.model.Photo
import com.antoniomy82.photogallery.utils.CommonUtil
import com.antoniomy82.photogallery.viewmodel.GalleryViewModel


class BaseFragment : Fragment() {

    private var fragmentBaseBinding: FragmentBaseBinding? = null
    private var galleryViewModel: GalleryViewModel? = null

    // ActivityForResult deprecated for library versions later than androidx.fragment:1.3.0-alpha04,use this
    @RequiresApi(Build.VERSION_CODES.M)
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            CommonUtil.requestCode?.let { galleryViewModel?.takePhotoForResult(it, data) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false)


        return fragmentBaseBinding?.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
                        it1, view, savedInstanceState, it2, startForResult
                    )
                }
            }
        }

        galleryViewModel?.setUI()



        //Observer Local repo full
        galleryViewModel?.retrieveLocalPhotos?.observe(viewLifecycleOwner){localRepo->
            var mNetwork:List<Photo>?=null

            galleryViewModel?.retrievePhotos?.observe(viewLifecycleOwner) {networkRepo->
                mNetwork=networkRepo
            }
            galleryViewModel?.checkRepositories(localRepo, mNetwork)
            fragmentBaseBinding?.progressBar?.visibility = View.GONE
        }

        //Set observer Local repo empty
        galleryViewModel?.retrievePhotos?.observe(viewLifecycleOwner) {networkRepo->
            var mLocal:List<Photo>?=null

            galleryViewModel?.retrieveLocalPhotos?.observe(viewLifecycleOwner){localRepo->
                mLocal=localRepo
            }
            galleryViewModel?.checkRepositories(mLocal, networkRepo)
            fragmentBaseBinding?.progressBar?.visibility = View.GONE
        }

    }
}