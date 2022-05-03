package com.amary.sisosmed.presentation.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
import com.amary.sisosmed.R
import com.amary.sisosmed.base.BaseFragment
import com.amary.sisosmed.core.Resource
import com.amary.sisosmed.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate),
    TextWatcher {

    private val viewModel: RegisterViewModel by viewModel()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            txtName.editText?.addTextChangedListener(this@RegisterFragment)
            txtEmail.editText?.addTextChangedListener(this@RegisterFragment)
            txtPassword.editText?.addTextChangedListener(this@RegisterFragment)

            btnLoginAsk.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
            }

            btnRegister.setOnClickListener {
                progressDialog.show()
                viewModel.register(
                    txtName.editText?.text.toString(),
                    txtEmail.editText?.text.toString(),
                    txtPassword.editText?.text.toString(),
                ).observe(viewLifecycleOwner){
                    when(it){
                        is Resource.Loading -> progressDialog.show()
                        is Resource.Success -> {
                            progressDialog.dismiss()
                            snackBar.make(true, getString(R.string.msg_register_success)).show()
                            findNavController().navigate(R.id.action_navigation_register_to_navigation_login)
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
            btnRegister.isEnabled = !txtName.isErrorEnabled && !txtEmail.isErrorEnabled && !txtPassword.isErrorEnabled &&
                    txtName.editText?.text.toString().isNotEmpty() && txtEmail.editText?.text.toString().isNotEmpty() &&
                    txtPassword.editText?.text.toString().isNotEmpty()
        }
    }

}