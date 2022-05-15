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
import com.amary.sisosmed.presentation.util.setMapStyle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapsFragment : BaseFragment<FragmentMapsBinding>(FragmentMapsBinding::inflate), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val viewModel: MapsViewModel by viewModel()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                getMyLocation()
            }
        }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            mapView.onCreate(savedInstanceState)
            mapView.onResume()
            MapsInitializer.initialize(requireActivity().applicationContext)
            mapView.getMapAsync(this@MapsFragment)
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
                    }
                }
                else -> {}
            }
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
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            mMap.isMyLocationEnabled = true
            val fused = LocationServices.getFusedLocationProviderClient(requireContext())
            fused.lastLocation.addOnSuccessListener {
                if (it != null) {
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                it.latitude,
                                it.longitude
                            ), 8f
                        )
                    )
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getObserveData(mMap)
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