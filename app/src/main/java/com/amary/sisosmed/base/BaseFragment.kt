package com.amary.sisosmed.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.amary.sisosmed.presentation.dialog.ProgressDialog
import com.amary.sisosmed.presentation.dialog.SnackBarCustom

abstract class BaseFragment<VB: ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding : VB? = null
    protected val binding get() = _binding!!
    protected val progressDialog: ProgressDialog by lazy { ProgressDialog(requireContext()) }
    protected val snackBar: SnackBarCustom by lazy { SnackBarCustom(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    open fun observeLiveData() = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        observeLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}