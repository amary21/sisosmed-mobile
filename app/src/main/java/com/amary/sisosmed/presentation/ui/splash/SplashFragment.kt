package com.amary.sisosmed.presentation.ui.splash

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentSplashBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private val viewModel: SplashViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        viewModel.getLocal.observe(viewLifecycleOwner){
            localization(it)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkAuthToken.observe(viewLifecycleOwner){
                when(it){
                    is Resource.Loading -> {}
                    is Resource.Success -> if (it.data == true) findNavController().navigate(R.id.action_navigation_splash_to_navigation_home)
                    is Resource.Unauthorized -> {
                        val extras = FragmentNavigatorExtras(binding.icApp to KeyValue.ICON_LOGIN)
                        findNavController().navigate(R.id.action_navigation_splash_to_navigation_login, null, null, extras)
                    }
                    else -> snackBar.make(false, getString(R.string.msg_no_connection)).show()
                }
            }
        }, KeyValue.TIME_PAUSE)
    }

    @SuppressLint("AppBundleLocaleChanges")
    @Suppress("DEPRECATION")
    private fun localization(locale: String){
        val language = if (locale != EmptyValue.STRING) locale else Locale.getDefault().language
        val newLocale = Locale(language)
        Locale.setDefault(newLocale)

        val config = Configuration()
        config.locale = newLocale
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }
}