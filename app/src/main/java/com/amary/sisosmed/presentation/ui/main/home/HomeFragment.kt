package com.amary.sisosmed.presentation.ui.main.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.databinding.FragmentHomeBinding
import com.amary.sisosmed.databinding.FragmentHomeContentBinding
import com.amary.sisosmed.presentation.adapter.PagerAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()
    private val adapter: PagerAdapter by lazy { PagerAdapter {
        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
    }}

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lyContent.rvHome.layoutManager = LinearLayoutManager(requireContext())
            lyContent.rvHome.adapter = adapter

            viewModel.userName.observe(viewLifecycleOwner){
                tvName.text = it
            }
        }

        viewModel.allStories().observe(viewLifecycleOwner){
            adapter.submitData(lifecycle, it)
        }

    }
}