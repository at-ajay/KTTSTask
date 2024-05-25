package com.kttstask.presentation.screens.auth_screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kttstask.R
import com.kttstask.databinding.FragmentCreateAccountBinding

class CreateAccountFragment: Fragment(R.layout.fragment_create_account) {

    private var createAccountBinding: FragmentCreateAccountBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAccountBinding = FragmentCreateAccountBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        createAccountBinding = null
    }

}