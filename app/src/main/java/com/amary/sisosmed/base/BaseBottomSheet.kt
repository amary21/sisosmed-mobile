package com.amary.sisosmed.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.amary.sisosmed.presentation.dialog.ProgressDialog
import com.amary.sisosmed.presentation.dialog.SnackBarCustom
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet<VB: ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BottomSheetDialogFragment() {
    private var _binding : VB? = null
    protected val binding get() = _binding
    protected val progressDialog: ProgressDialog by lazy { ProgressDialog(requireContext()) }
    protected val snackBar: SnackBarCustom by lazy { SnackBarCustom(requireActivity()) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (setExpandLayout()){
            val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheet.setOnShowListener {
                val dialog = it as BottomSheetDialog
                val dialogView = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
                val dialogBehaviour = BottomSheetBehavior.from(dialogView)
                val layoutParams = dialogView.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                dialogView.layoutParams = layoutParams
                dialogBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
            return bottomSheet
        }

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding?.root
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    abstract fun setExpandLayout(): Boolean

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}