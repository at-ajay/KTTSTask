package com.kttstask.presentation.screens.location_screens

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kttstask.App
import com.kttstask.R
import com.kttstask.databinding.FragmentHomeBinding
import com.kttstask.service.LocationService


class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var userAdapter: UserAdapter

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding = FragmentHomeBinding.bind(view)

        val viewModel: HomeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val service = Intent(requireActivity(), LocationService::class.java)

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

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                Toast.makeText(requireActivity(), "Please enable location permission from application settings page", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Please enable location permission", Toast.LENGTH_SHORT).show()
            }

        } else {
            requireActivity().startService(service)
        }

        viewModel.getUsers()
        viewModel.getLocationHistory()
        homeBinding.tvProfileChar.text = viewModel.users.filter { it._id == App.userId }[0].fullName[0].toString().uppercase()
        val historyAdapter = LocationHistoryAdapter(history = viewModel.historyList, navController = findNavController())

        if (viewModel.historyList.isEmpty()) {
            homeBinding.locationHistory.visibility = View.GONE
            homeBinding.historyLabel.visibility = View.GONE
            homeBinding.tvNoData.visibility = View.VISIBLE
        } else {
            homeBinding.locationHistory.visibility = View.VISIBLE
            homeBinding.historyLabel.visibility = View.VISIBLE
            homeBinding.tvNoData.visibility = View.GONE

            homeBinding.locationHistory.layoutManager = LinearLayoutManager(requireActivity())
            homeBinding.locationHistory.adapter = historyAdapter
        }

        userAdapter = UserAdapter(
            users = viewModel.users,
            logoutClickListener =  { user ->
                viewModel.logoutUser(user)
                viewModel.users.removeIf { it._id == user._id }
                userAdapter.notifyDataSetChanged()
                 if (viewModel.users.size == 0) {
                     findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                 } else {
                     App.userId = viewModel.users[0]._id
                 }
            },
            onSwitchUserClicked = { user ->
                viewModel.updateCurrentUserSession(user = user)
                homeBinding.userList.visibility = View.GONE

                viewModel.getLocationHistory()
                historyAdapter.history = viewModel.historyList
                historyAdapter.notifyDataSetChanged()

                userAdapter.users = viewModel.users
                userAdapter.notifyDataSetChanged()
            }
        )
        homeBinding.userRecycler.layoutManager = LinearLayoutManager(requireActivity())
        homeBinding.userRecycler.adapter = userAdapter

        homeBinding.btnProfile.setOnClickListener {
            if (homeBinding.userList.visibility == View.VISIBLE) {
                homeBinding.userList.visibility = View.GONE
            } else {
                homeBinding.userList.visibility = View.VISIBLE
            }
        }

        homeBinding.refreshBtn.setOnClickListener {
            val oldList = viewModel.historyList
            viewModel.getLocationHistory()

            if (oldList.size == viewModel.historyList.size) {
                Toast.makeText(requireActivity(), "No data available to refresh", Toast.LENGTH_SHORT).show()
            } else {
                historyAdapter.history = viewModel.historyList
                historyAdapter.notifyDataSetChanged()
                Toast.makeText(requireActivity(), "Location data updated", Toast.LENGTH_SHORT).show()
            }
        }

        homeBinding.addNewUser.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }

        homeBinding.quitBtn.setOnClickListener {
            requireActivity().stopService(service)
            requireActivity().finish()
        }

    }

}