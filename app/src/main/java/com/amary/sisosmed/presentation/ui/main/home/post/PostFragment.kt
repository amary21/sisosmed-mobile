package com.amary.sisosmed.presentation.ui.main.home.post

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseBottomSheet
import com.amary.sisosmed.constant.DataStore
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentPostBinding
import com.amary.sisosmed.presentation.util.createCustomTempFile
import com.amary.sisosmed.presentation.util.reduceFileImage
import com.amary.sisosmed.presentation.util.rotateBitmap
import com.amary.sisosmed.presentation.util.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PostFragment: BaseBottomSheet<FragmentPostBinding>(FragmentPostBinding::inflate), TextWatcher {
    private val viewModel: PostViewModel by viewModel()
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), true)
            viewModel.setImgBitmap(myFile)
            binding?.placeHolder?.isVisible = false
            binding?.imgPreview?.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())
            val result = BitmapFactory.decodeFile(myFile.path)
            viewModel.setImgBitmap(myFile)
            binding?.placeHolder?.isVisible = false
            binding?.imgPreview?.setImageBitmap(result)
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission ->
        permission.entries.forEach {
            if (it.key == Manifest.permission.CAMERA && it.value == true){
                Toast.makeText(requireContext(), getString(R.string.msg_camera_granted), Toast.LENGTH_SHORT).show()
            } else if (it.key == Manifest.permission.CAMERA && it.value == false) {
                Toast.makeText(requireContext(), getString(R.string.msg_camera_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setExpandLayout() = true

    override fun initView(view: View, savedInstanceState: Bundle?) {
        checkForm()
        if (!allPermissionsGranted()){
            requestPermission.launch(REQUIRED_PERMISSIONS)
        }

        binding?.apply {
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.inflateMenu(R.menu.post_menu)
            toolbar.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.toolbar_camera -> {
                        if (allPermissionsGranted()){
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            intent.resolveActivity(requireActivity().packageManager)
                            createCustomTempFile(requireActivity().application).also {
                                val photoUri = FileProvider.getUriForFile(
                                    requireActivity(),
                                    DataStore.STORE_NAME,
                                    it
                                )
                                currentPhotoPath = it.absolutePath
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                launcherIntentCamera.launch(intent)
                            }
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.msg_camera_denied), Toast.LENGTH_SHORT).show()
                        }
                    }
                    R.id.toolbar_gallery -> {
                        val intent = Intent(ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        val chooser = Intent.createChooser(intent, "Choose a Picture")
                        launcherIntentGallery.launch(chooser)
                    }
                }
                return@setOnMenuItemClickListener false
            }

            txtPost.editText?.addTextChangedListener(this@PostFragment)
            btnPost.setOnClickListener {
                progressDialog.show()
                if (getFile != null){
                    val file = reduceFileImage(getFile as File)
                    viewModel.post(txtPost.editText?.text.toString(), file).observe(viewLifecycleOwner){ result ->
                        when(result){
                            is Resource.Loading -> progressDialog.show()
                            is Resource.Success -> {
                                progressDialog.dismiss()
                                snackBar.make(true, getString(R.string.msg_post_success)).show()
                                findNavController().popBackStack(R.id.navigation_home, true)
                                findNavController().navigate(R.id.navigation_home)
                            }
                            is Resource.Unauthorized -> {
                                progressDialog.dismiss()
                                snackBar.make(false, result.message.toString()).show()
                                findNavController().popBackStack(R.id.navigation_login, false)
                            }
                            else -> {
                                progressDialog.dismiss()
                                snackBar.make(false, result.message.toString()).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkForm(){
        viewModel.getImgBitmap().observe(viewLifecycleOwner){
            binding?.btnPost?.isEnabled = !(it == null || binding?.txtPost?.editText?.text.toString().isEmpty())
            getFile = it
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) { checkForm() }
}