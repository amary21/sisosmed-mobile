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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseBottomSheet
import com.amary.sisosmed.constant.DataStore
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentPostBinding
import com.amary.sisosmed.presentation.util.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PostFragment: BaseBottomSheet<FragmentPostBinding>(FragmentPostBinding::inflate), TextWatcher, OnMapReadyCallback {
    private val viewModel: PostViewModel by viewModel()
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var mMap: GoogleMap

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
            if (it.key == Manifest.permission.CAMERA && it.value){
                Toast.makeText(requireContext(), getString(R.string.msg_camera_granted), Toast.LENGTH_SHORT).show()
            } else if (it.key == Manifest.permission.CAMERA && !it.value) {
                Toast.makeText(requireContext(), getString(R.string.msg_camera_denied), Toast.LENGTH_SHORT).show()
            }

            if(it.key == Manifest.permission.ACCESS_FINE_LOCATION && it.value){
                getMyLocation()
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

            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            MapsInitializer.initialize(requireActivity().applicationContext)
            mapView.getMapAsync(this@PostFragment)

            txtPost.editText?.addTextChangedListener(this@PostFragment)
            btnPost.setOnClickListener {
                progressDialog.show()
                if (getFile != null){
                    viewModel.getMyLocation().observe(viewLifecycleOwner){ location ->
                        val file = reduceFileImage(getFile as File)
                        viewModel.post(
                            txtPost.editText?.text.toString(),
                            file,
                            location.latitude.toFloat(),
                            location.longitude.toFloat()
                        ).observe(viewLifecycleOwner){ result ->
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

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(REQUIRED_PERMISSIONS)
        } else {
            mMap.isMyLocationEnabled = true
            val fused = LocationServices.getFusedLocationProviderClient(requireContext())
            fused.lastLocation.addOnSuccessListener {
                if (it != null) {
                    viewModel.setMyLocation(it)
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                it.latitude,
                                it.longitude
                            ), 15f
                        )
                    )
                }
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) { checkForm() }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle(requireContext(), mMap)
    }

    override fun onResume() {
        super.onResume()
        binding?.mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding?.mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding?.mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.mapView?.onLowMemory()
    }
}