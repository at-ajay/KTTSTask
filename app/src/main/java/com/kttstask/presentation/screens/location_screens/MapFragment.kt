package com.kttstask.presentation.screens.location_screens

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.kttstask.R
import com.kttstask.databinding.FragmentMapBinding
import com.kttstask.util.getFormattedDate

class MapFragment: Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private val args by navArgs<MapFragmentArgs>()
    private lateinit var mapBinding: FragmentMapBinding
    private lateinit var gMap: GoogleMap

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapBinding = FragmentMapBinding.bind(view)
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)

        mapBinding.btnPlayback.visibility = if (args.historyList.size > 1) View.VISIBLE else View.GONE

        mapBinding.btnPlayback.setOnClickListener {
            gMap.clear()

            val latLngBound = LatLngBounds.Builder()
            args.historyList.forEach { location ->
                gMap.addMarker(
                    MarkerOptions().position(LatLng(location.latitude, location.longitude)).title(getFormattedDate(location.time))
                )
                latLngBound.include(LatLng(location.latitude, location.longitude))
                Log.d("Marker Data", "Latitude ${location.latitude} Longitude : ${location.longitude} Time : ${getFormattedDate(location.time)}")
            }

            gMap.addPolyline(PolylineOptions().addAll(args.historyList.map { LatLng(it.latitude, it.longitude) }))

            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBound.build(), 50)
            gMap.animateCamera(cameraUpdate)

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(map: GoogleMap) {
        gMap = map

        val selectedLocation = LatLng(args.selectedLocation.latitude, args.selectedLocation.longitude)
        map.addMarker(
            MarkerOptions().position(selectedLocation).title(getFormattedDate(args.selectedLocation.time))
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 20f))
    }

}