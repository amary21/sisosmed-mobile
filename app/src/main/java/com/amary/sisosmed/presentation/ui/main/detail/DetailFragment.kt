package com.amary.sisosmed.presentation.ui.main.detail

import android.os.Bundle
import android.view.View
import com.amary.sisosmed.base.BaseBottomSheet
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.databinding.FragmentDetailBinding
import com.amary.sisosmed.domain.model.Story
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment: BaseBottomSheet<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val viewModel: DetailViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val story = arguments?.getSerializable(KeyValue.BUNDLE_ITEM) as Story

            toolbar.title = story.name
            toolbar.setNavigationOnClickListener { dismiss() }

            tvCreated.text = convertDate(story.createdAt)
            tvDescription.text = story.description

            Glide.with(requireContext())
                .load(story.photoUrl)
                .into(imgPhoto)
        }
    }

    private fun convertDate(createdAt: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val date = parser.parse(createdAt)
        val dateFormat = SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault())
        return dateFormat.format(date!!)
    }
}