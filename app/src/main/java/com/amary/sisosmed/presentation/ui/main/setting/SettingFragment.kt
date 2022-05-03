package com.amary.sisosmed.presentation.ui.main.setting

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.databinding.FragmentSettingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {
    private val viewModel: SettingViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnLogout.setOnClickListener {
                progressDialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.logout.observe(viewLifecycleOwner){
                        progressDialog.dismiss()
                        findNavController().navigate(R.id.action_navigation_setting_to_navigation_login)
                    }
                }, KeyValue.TIME_PAUSE)
            }
        }
    }
}