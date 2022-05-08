package com.amary.sisosmed.presentation.ui.main.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate) {
    private val viewModel: MapsViewModel by viewModel()
    private fun requestPermissionLauncher(mMap: GoogleMap) = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            getMyLocation(mMap)
        }
    }

    private fun getMyLocation(mMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher(mMap).launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            mMap.isMyLocationEnabled = true
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            MapsInitializer.initialize(requireActivity().applicationContext)
            mapView.getMapAsync { mMap ->
                mMap.uiSettings.isZoomControlsEnabled = true
                mMap.uiSettings.isIndoorLevelPickerEnabled = true
                mMap.uiSettings.isCompassEnabled = true
                mMap.uiSettings.isMapToolbarEnabled = true

                getObserveData(mMap)
                getMyLocation(mMap)
            }
        }
    }

    private fun getObserveData(mMap: GoogleMap) {
        viewModel.getDataWithLoc.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.forEach { story ->
                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(story.lat, story.lon))
                                .title(story.name)
                                .snippet(story.description)

                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(story.lat, story.lon), 15f))
                    }
                }
                else -> {}
            }
        }
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