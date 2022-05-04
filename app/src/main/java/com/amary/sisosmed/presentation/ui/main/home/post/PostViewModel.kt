package com.amary.sisosmed.presentation.ui.main.home.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amary.sisosmed.domain.usecase.UseCase
import java.io.File

class PostViewModel(private val useCase: UseCase): ViewModel() {
    private val imgBitmap = MutableLiveData<File>()

    fun getImgBitmap(): LiveData<File> = imgBitmap
    fun setImgBitmap(file: File) { imgBitmap.postValue(file) }

    fun post(description: String, imgFile: File) = useCase.post(file = imgFile, description = description).asLiveData()
}