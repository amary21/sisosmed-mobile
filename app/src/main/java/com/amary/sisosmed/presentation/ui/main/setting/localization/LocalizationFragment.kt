package com.amary.sisosmed.presentation.ui.main.setting.localization

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseBottomSheet
import com.amary.sisosmed.constant.EmptyValue
import com.amary.sisosmed.constant.KeyValue
import com.amary.sisosmed.databinding.FragmentLocalizationBinding
import com.amary.sisosmed.presentation.adapter.LocalAdapter
import com.amary.sisosmed.presentation.ui.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalizationFragment: BaseBottomSheet<FragmentLocalizationBinding>(FragmentLocalizationBinding::inflate) {
    private val viewModel: LocalizationViewModel by viewModel()
    private val adapter: LocalAdapter by lazy { LocalAdapter{
        viewModel.setLocal(it.code).observe(viewLifecycleOwner){ result ->
            if (it.code == result){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.title_restart_app))
                    .setMessage(getString(R.string.msg_restart_app))
                    .setPositiveButton(getString(R.string.title_restart)){ _, _ ->
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        requireContext().startActivity(intent)
                        kotlin.system.exitProcess(0)
                    }.setNegativeButton(getString(R.string.title_later)){ _, _ ->
                        findNavController().popBackStack(R.id.navigation_setting, true)
                        findNavController().navigate(R.id.navigation_setting)
                    }.show()
            }
        }
    } }

    override fun setExpandLayout() = false
    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            val localSelected = arguments?.getString(KeyValue.BUNDLE_LOCAL) ?: EmptyValue.DEFAULT
            adapter.selected = localSelected

            rvLocal.layoutManager = LinearLayoutManager(requireContext())
            rvLocal.adapter = adapter

            viewModel.dataLocal.observe(viewLifecycleOwner){
                adapter.submitList(it)
            }
        }
    }

}