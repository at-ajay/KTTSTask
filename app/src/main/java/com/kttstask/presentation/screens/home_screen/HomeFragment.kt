package com.kttstask.presentation.screens.home_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kttstask.R
import com.kttstask.databinding.FragmentHomeBinding
import com.kttstask.service.LocationService


class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var homeBinding: FragmentHomeBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding = FragmentHomeBinding.bind(view)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d("Permission", "ACCESS_FINE_LOCATION Permission Granted")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d("Permission", "ACCESS_COARSE_LOCATION Permission Granted")
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                    Log.d("Permission", "ACCESS_BACKGROUND_LOCATION Permission Granted")
                }
                else -> {
                    Toast.makeText(requireActivity(), "Permission Not Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        locationPermissionRequest.launch(permissions)

        Intent(requireActivity(), LocationService::class.java).also {
            requireActivity().startService(it)
        }

        homeBinding.showMapBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    Toast.makeText(requireActivity(), "Please enable location permission from application settings page", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "Please enable location permission", Toast.LENGTH_SHORT).show()
                }

            } else {
                findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
            }
        }
    }

}