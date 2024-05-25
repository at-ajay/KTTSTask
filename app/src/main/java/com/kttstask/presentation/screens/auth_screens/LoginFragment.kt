package com.kttstask.presentation.screens.auth_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kttstask.R
import com.kttstask.databinding.FragmentLoginBinding
import com.kttstask.util.validateEmailAddress
import com.kttstask.util.validatePassword

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginBinding: FragmentLoginBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBinding = FragmentLoginBinding.bind(view)
        val viewModel: AuthViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        loginBinding.btnLogin.setOnClickListener {
            // Re-initialising error state
            loginBinding.email.error = null
            loginBinding.password.error = null

            val email = loginBinding.etEmail.text.toString().trim()
            val password = loginBinding.etPassword.text.toString().trim()

            val emailValidationResult = validateEmailAddress(email, true)
            val passwordValidationResult = validatePassword(password)

            if (emailValidationResult.isSuccess && passwordValidationResult.isSuccess) {
                if (viewModel.canLoginUser(email, password)) {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    Toast.makeText(requireActivity(), "Invalid credentials / User not available", Toast.LENGTH_SHORT).show()
                }
            } else {
                loginBinding.email.error = getString(emailValidationResult.errorMsgId!!)
                loginBinding.password.error = getString(passwordValidationResult.errorMsgId!!)
            }
        }

        loginBinding.btnForgetPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }

        loginBinding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }
    }

}