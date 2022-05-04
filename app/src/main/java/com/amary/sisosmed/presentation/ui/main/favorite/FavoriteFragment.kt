package com.amary.sisosmed.presentation.ui.main.favorite

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.databinding.FragmentFavoriteBinding
import com.amary.sisosmed.presentation.adapter.FavoriteAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val viewModel: FavoriteViewModel by viewModel()
    private val adapter: FavoriteAdapter by lazy { FavoriteAdapter{
        val bundle = bundleOf(KeyValue.BUNDLE_ITEM to it)
        findNavController().navigate(R.id.action_navigation_favorite_to_navigation_detail, bundle)
    }}

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(requireContext())
            rvFavorite.adapter = adapter

            viewModel.userName.observe(viewLifecycleOwner){ tvName.text = it }
            viewModel.allFavoriteStories.observe(viewLifecycleOwner){
                if (it.isNotEmpty()){
                    rvFavorite.isVisible = true
                    tvNotFound.isVisible = false
                    adapter.submitList(ArrayList(it))
                } else {
                    rvFavorite.isVisible = false
                    tvNotFound.isVisible = true
                }
            }
        }
    }
}