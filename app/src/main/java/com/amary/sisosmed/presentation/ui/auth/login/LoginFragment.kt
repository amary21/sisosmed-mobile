package com.amary.sisosmed.presentation.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentLoginBinding
import com.amary.sisosmed.presentation.dialog.SnackBarCustom
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate), TextWatcher {

    private val viewModel: LoginViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            txtEmail.editText?.addTextChangedListener(this@LoginFragment)
            txtPassword.editText?.addTextChangedListener(this@LoginFragment)

            btnRegisterAsk.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_login_to_navigation_register)
            }

            btnLogin.setOnClickListener {
                progressDialog.show()
                viewModel.login(
                    txtEmail.editText?.text.toString(),
                    txtPassword.editText?.text.toString()
                ).observe(viewLifecycleOwner){
                    when(it){
                        is Resource.Loading -> progressDialog.show()
                        is Resource.Success -> {
                            progressDialog.dismiss()
                            snackBar.make(true, getString(R.string.msg_login_success)).show()
                            findNavController().navigate(R.id.action_navigation_login_to_navigation_home)
                        }
                        else -> {
                            progressDialog.dismiss()
                            snackBar.make(false, it.message.toString()).show()
                        }
                    }
                }
            }
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        binding.apply {
            btnLogin.isEnabled =
                !txtEmail.isErrorEnabled && !txtPassword.isErrorEnabled && txtEmail.editText?.text.toString()
                    .isNotEmpty() && txtPassword.editText?.text.toString().isNotEmpty()
        }
    }

}