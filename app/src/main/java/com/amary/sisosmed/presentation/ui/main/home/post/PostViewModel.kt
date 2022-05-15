package com.amary.sisosmed.presentation.ui.main.home.post

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase
import java.io.File

class PostViewModel(private val useCase: UseCase): ViewModel() {
    private val imgBitmap = MutableLiveData<File>()
    private val myLocation = MutableLiveData<Location>()

    fun getImgBitmap(): LiveData<File> = imgBitmap
    fun setImgBitmap(file: File) { imgBitmap.postValue(file) }

    fun getMyLocation(): LiveData<Location> = myLocation
    fun setMyLocation(location: Location) { myLocation.postValue(location) }

    fun post(description: String, imgFile: File, lat: Float, lon: Float) =
        useCase.post(
            file = imgFile,
            description = description,
            lat = lat,
            lon = lon
        ).asLiveData()
}