package com.kttstask.presentation.screens.auth_screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kttstask.R
import com.kttstask.databinding.FragmentCreateAccountBinding
import com.kttstask.util.validateEmailAddress
import com.kttstask.util.validateName
import com.kttstask.util.validatePassword
import com.kttstask.util.validateRepeatedPassword

class CreateAccountFragment: Fragment(R.layout.fragment_create_account) {

    private lateinit var createAccountBinding: FragmentCreateAccountBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAccountBinding = FragmentCreateAccountBinding.bind(view)
        val viewModel: AuthViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        createAccountBinding.btnCreateAccount.setOnClickListener {
            // Re-initialising error state
            createAccountBinding.fullName.error = null
            createAccountBinding.email.error = null
            createAccountBinding.password.error = null
            createAccountBinding.repeatPassword.error = null

            val fullName = createAccountBinding.etFullName.text.toString().trim()
            val email = createAccountBinding.etEmail.text.toString().trim()
            val password = createAccountBinding.etPassword.text.toString().trim()
            val repeatedPassword = createAccountBinding.etRepeatPassword.text.toString().trim()

            val nameValidationResult = validateName(fullName)
            val emailValidationResult = validateEmailAddress(email)
            val passwordValidationResult = validatePassword(password)
            val repeatedPasswordValidationResult = validateRepeatedPassword(password, repeatedPassword)

            if (nameValidationResult.isSuccess && emailValidationResult.isSuccess && passwordValidationResult.isSuccess && repeatedPasswordValidationResult.isSuccess) {
                viewModel.createUser(fullName, email, password)
                Toast.makeText(activity, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createAccountFragment_to_homeFragment)
            } else {
                nameValidationResult.errorMsgId?.let {
                    createAccountBinding.fullName.error = getString(nameValidationResult.errorMsgId)
                }
                emailValidationResult.errorMsgId?.let {
                    createAccountBinding.email.error = getString(emailValidationResult.errorMsgId)
                }
                passwordValidationResult.errorMsgId?.let {
                    createAccountBinding.password.error = getString(passwordValidationResult.errorMsgId)
                }
                repeatedPasswordValidationResult.errorMsgId?.let {
                    createAccountBinding.repeatPassword.error = getString(repeatedPasswordValidationResult.errorMsgId)
                }
            }
        }
    }

}