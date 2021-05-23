package com.antoniomy82.photogallery.ui


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.antoniomy82.photogallery.R
import com.antoniomy82.photogallery.utils.CommonUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load base fragment
        CommonUtil.replaceFragment(BaseFragment(), supportFragmentManager)
    }

}