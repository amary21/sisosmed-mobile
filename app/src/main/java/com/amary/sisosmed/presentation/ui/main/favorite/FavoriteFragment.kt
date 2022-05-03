package com.amary.sisosmed.presentation.ui.main.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.databinding.FragmentFavoriteBinding
import com.amary.sisosmed.presentation.adapter.PagerAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val viewModel: FavoriteViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel.userName.observe(viewLifecycleOwner){ tvName.text = it }
        }
    }
}