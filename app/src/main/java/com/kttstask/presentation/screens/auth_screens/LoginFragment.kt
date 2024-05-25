package com.kttstask.presentation.screens.auth_screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kttstask.R
import com.kttstask.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var loginBinding: FragmentLoginBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginBinding = FragmentLoginBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginBinding = null
    }

}