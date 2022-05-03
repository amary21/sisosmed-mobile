package com.amary.sisosmed.presentation.ui.main.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentHomeBinding
import com.amary.sisosmed.presentation.adapter.PagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()
    private val adapter: PagerAdapter by lazy { PagerAdapter {
        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
    }}

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel.userName.observe(viewLifecycleOwner){
                tvName.text = it
            }

            lyContent.rvHome.layoutManager = LinearLayoutManager(requireContext())
            lyContent.rvHome.adapter = adapter
            adapter.addLoadStateListener {
                viewModel.pagerResource(it).observe(viewLifecycleOwner){ state ->
                    when(state){
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            lyContent.rvHome.isVisible = true
                            lyContent.tvNotFound.isVisible = false
                        }
                        is Resource.Failed -> {
                            if (adapter.itemCount == 0){
                                lyContent.rvHome.isVisible = false
                                lyContent.tvNotFound.isVisible = true
                            }
                        }
                        is Resource.Unauthorized -> {
                            viewModel.logout.observe(viewLifecycleOwner){
                                snackBar.make(false, state.message.toString()).show()
                                findNavController().navigate(R.id.action_navigation_setting_to_navigation_login)
                            }
                        }
                        is Resource.ServerError -> {
                            snackBar.make(false, state.message.toString()).show()
                        }
                    }
                }
            }

            viewModel.allStories().observe(viewLifecycleOwner){
                adapter.submitData(lifecycle, it)
            }
        }
    }
}