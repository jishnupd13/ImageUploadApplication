package com.app.mymainapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.mymainapp.databinding.ActivitySplashScreenBinding
import com.app.mymainapp.ui.imageupload.ImageUploadActivity
import com.app.mymainapp.viewmodels.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity(), View.OnClickListener {


    private val binding: ActivitySplashScreenBinding by viewBinding()
    private val viewModel: SplashScreenViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.listener = this
        binding.viewModel = viewModel


        Handler(Looper.getMainLooper()).postDelayed({

            Intent(
                this@SplashScreenActivity,
                ImageUploadActivity::class.java
            ).apply {
                startActivity(this)
                overridePendingTransition(0, 0)
                finish()
            }

        }, 3000)

    }

    override fun onClick(view: View?) {

    }
}