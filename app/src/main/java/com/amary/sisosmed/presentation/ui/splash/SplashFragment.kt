package com.amary.sisosmed.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private val viewModel: SplashViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewModel.checkAuthToken.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {}
                is Resource.Success -> findNavController().navigate(R.id.action_navigation_splash_to_navigation_home)
                else -> {
                    snackBar.make(false, it.message.toString()).show()
                    findNavController().navigate(R.id.action_navigation_splash_to_navigation_login)
                }
            }
        }
    }
}