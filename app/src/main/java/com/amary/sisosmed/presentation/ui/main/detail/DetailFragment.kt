package com.amary.sisosmed.presentation.ui.main.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseBottomSheet
import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.databinding.FragmentDetailBinding
import com.amary.sisosmed.domain.model.Story
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment: BaseBottomSheet<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val viewModel: DetailViewModel by viewModel()

    override fun setExpandLayout() = true

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            val story = arguments?.getSerializable(KeyValue.BUNDLE_ITEM) as Story

            toolbar.title = story.name
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.inflateMenu(R.menu.favorite_menu)
            toolbar.setOnMenuItemClickListener { menuItem ->
                if (menuItem.itemId == R.id.toolbar_favorite){
                    viewModel.setFavorite(story).observe(viewLifecycleOwner){
                        if (it){
                            menuItem.setIcon(R.drawable.ic_favorite)
                        } else {
                            menuItem.setIcon(R.drawable.ic_favorite_border)
                        }
                    }
                }

                return@setOnMenuItemClickListener false
            }


            viewModel.isFavorite(story.id).observe(viewLifecycleOwner){
                val menuFavorite = toolbar.menu.findItem(R.id.toolbar_favorite)
                if (it){
                    menuFavorite.setIcon(R.drawable.ic_favorite)
                } else {
                    menuFavorite.setIcon(R.drawable.ic_favorite_border)
                }
            }

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
        return date?.let { dateFormat.format(it) } ?: EmptyValue.STRING
    }
}