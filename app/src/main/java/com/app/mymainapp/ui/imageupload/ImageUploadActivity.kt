package com.app.mymainapp.ui.imageupload

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.mymainapp.R
import com.app.mymainapp.databinding.ActivityImageUploadBinding
import com.app.mymainapp.utils.hide
import com.app.mymainapp.utils.show
import com.app.mymainapp.utils.showToast
import com.app.mymainapp.viewmodels.ImageUploadViewModel
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ImageUploadActivity : AppCompatActivity(), View.OnClickListener {


    private val binding: ActivityImageUploadBinding by viewBinding()
    private val viewModel: ImageUploadViewModel by viewModels()

    private var resultFilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.listener = this
        binding.viewModel = viewModel
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data


            try {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val fileUri = data?.data!!

                        Glide.with(this)
                            .load(fileUri)
                            .placeholder(R.drawable.user_image)
                            .error(R.drawable.user_image)
                            .into(binding.uploadImage)

                        resultFilePath = fileUri.path ?: ""


                    }
                    ImagePicker.RESULT_ERROR -> {
                        showToast(ImagePicker.getError(data = data))
                    }
                    else -> {
                        showToast("Task Cancelled")

                    }
                }
            } catch (e: Exception) {

                showToast("Oops! Something went wrong")
            }

        }


    override fun onClick(view: View?) {

        when (view) {
            binding.uploadImage -> {
                ImagePicker.with(this)
                    .crop()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            binding.textUploadImage -> {
                
                if(resultFilePath.isNotEmpty()){

                    binding.progressCircular.show()
                    binding.uploadImage.hide()
                    binding.textUploadImage.hide()
                    showToast("Waiting")

                   val uploadRequestId= MediaManager.get()
                       .upload(resultFilePath)
                       .callback(object :UploadCallback{
                           override fun onStart(requestId: String?) {

                               showToast("Uploading started")
                               binding.progressCircular.show()
                               binding.uploadImage.hide()
                               binding.textUploadImage.hide()
                           }

                           @RequiresApi(Build.VERSION_CODES.N)
                           override fun onProgress(
                               requestId: String?,
                               bytes: Long,
                               totalBytes: Long
                           ) {

                               binding.progressCircular.max=100


                               val progress = ((bytes.toDouble() / totalBytes)*100).toInt()
                               Timber.e("<<<< $progress")
                               binding.progressCircular.setProgress(progress,true)
                           }

                           override fun onSuccess(
                               requestId: String?,
                               resultData: MutableMap<Any?, Any?>?
                           ) {

                               binding.progressCircular.hide()
                               binding.uploadImage.show()
                               binding.textUploadImage.show()

                               resultFilePath = ""
                               Glide.with(applicationContext)
                                   .load(R.drawable.user_image)
                                   .placeholder(R.drawable.user_image)
                                   .error(R.drawable.user_image)
                                   .into(binding.uploadImage)

                               showToast("Uploaded")

                           }

                           override fun onError(requestId: String?, error: ErrorInfo?) {
                              showToast(error?.description?:"")
                               binding.progressCircular.hide()
                               binding.uploadImage.show()
                               binding.textUploadImage.show()
                           }

                           override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                               showToast(error?.description?:"")
                               binding.progressCircular.hide()
                               binding.uploadImage.show()
                               binding.textUploadImage.show()
                           }

                       })
                       .dispatch()

                }else
                    showToast("Please select image")

            }
        }
    }
}