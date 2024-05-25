package com.kttstask.presentation.screens.auth_screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kttstask.R
import com.kttstask.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment: Fragment(R.layout.fragment_forget_password) {

    private var forgetPasswordBinding: FragmentForgetPasswordBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forgetPasswordBinding = FragmentForgetPasswordBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        forgetPasswordBinding = null
    }

}