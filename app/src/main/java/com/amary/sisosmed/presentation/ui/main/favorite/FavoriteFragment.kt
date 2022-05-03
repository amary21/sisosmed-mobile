package com.amary.sisosmed.presentation.ui.main.favorite

import android.os.Bundle
import android.view.View
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val viewModel: FavoriteViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel.userName.observe(viewLifecycleOwner){ tvName.text = it }
        }
    }
}