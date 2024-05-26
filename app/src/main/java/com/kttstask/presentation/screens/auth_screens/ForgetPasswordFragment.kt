package com.kttstask.presentation.screens.auth_screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kttstask.R
import com.kttstask.databinding.FragmentForgetPasswordBinding
import com.kttstask.util.validateEmailAddress
import com.kttstask.util.validatePassword
import com.kttstask.util.validateRepeatedPassword

class ForgetPasswordFragment: Fragment(R.layout.fragment_forget_password) {

    private lateinit var forgetPasswordBinding: FragmentForgetPasswordBinding
    private lateinit var email: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forgetPasswordBinding = FragmentForgetPasswordBinding.bind(view)
        val viewModel: AuthViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        forgetPasswordBinding.btnCheckEmail.setOnClickListener {
            forgetPasswordBinding.email.error = null

            email = forgetPasswordBinding.etEmail.text.toString().trim()
            val emailValidationResult = validateEmailAddress(email, true)

            if (emailValidationResult.isSuccess) {
                if (viewModel.isEmailExists(email)) {
                    forgetPasswordBinding.sectionEmail.visibility = View.GONE
                    forgetPasswordBinding.sectionPassword.visibility = View.VISIBLE
                } else {
                    forgetPasswordBinding.email.error = getString(R.string.err_email_not_exists)
                }
            } else {
                forgetPasswordBinding.email.error = getString(emailValidationResult.errorMsgId!!)
            }
        }

        forgetPasswordBinding.btnChangePassword.setOnClickListener {
            //Re-initialising error state
            forgetPasswordBinding.newPassword.error = null
            forgetPasswordBinding.repeatNewPassword.error = null

            val password = forgetPasswordBinding.etPassword.text.toString().trim()
            val repeatedPassword = forgetPasswordBinding.etRepeatPassword.text.toString().trim()

            val passwordValidationResult = validatePassword(password)
            val repeatedPasswordValidationResult = validateRepeatedPassword(password, repeatedPassword)

            if (passwordValidationResult.isSuccess && repeatedPasswordValidationResult.isSuccess) {
                if (!viewModel.updateUserPassword(email, password)) {
                    Toast.makeText(requireActivity(), getString(R.string.err_same_as_old_password), Toast.LENGTH_SHORT).show()
                } else {
                    findNavController().navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
                }
            } else {
                passwordValidationResult.errorMsgId?.let {
                    forgetPasswordBinding.newPassword.error = getString(passwordValidationResult.errorMsgId)
                }
                repeatedPasswordValidationResult.errorMsgId?.let {
                    forgetPasswordBinding.repeatNewPassword.error = getString(repeatedPasswordValidationResult.errorMsgId)
                }
            }
        }

    }

}