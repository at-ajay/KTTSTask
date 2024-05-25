package com.kttstask.presentation.screens.home_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kttstask.R
import com.kttstask.databinding.FragmentHomeBinding


class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var homeBinding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding = FragmentHomeBinding.bind(view)

        homeBinding.showMapBtn.setOnClickListener {

        }
    }

}