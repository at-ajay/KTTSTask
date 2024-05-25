package com.kttstask.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.kttstask.App
import com.kttstask.R
import com.kttstask.data.models.LocationInfo
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LocationService: Service(), LocationListener {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val notificationChannelId = "location_channel"
    private val notificationId = 1

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(notificationId, createNotification())
        requestLocationUpdates()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 15000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, this, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            notificationChannelId,
            "Location Updates",
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)

        val builder = Notification.Builder(applicationContext, notificationChannelId)
            .setContentTitle("Location Service")
            .setContentText("Getting location updates")
            .setChannelId(notificationChannelId)
            .setSmallIcon(R.drawable.ic_android)
            .setPriority(Notification.PRIORITY_LOW)

        return builder.build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(this)
        job.cancel()
        Log.d("Location Info", "Service Stopped")
    }

    override fun onLocationChanged(location: Location) {
        Log.d("Location Info", "${location.latitude} ${location.longitude}")
        val locationDataSearchResult = App.realm.query<LocationInfo>(
            query = "userId == $0",
            args = arrayOf(App.userId)
        ).find()

        val locData = com.kttstask.data.models.Location().apply {
            this.time = System.currentTimeMillis()
            this.latitude = location.latitude
            this.longitude = location.longitude
        }

        scope.launch {
            App.realm.write {

                if (locationDataSearchResult.size > 0) {
                    findLatest(locationDataSearchResult.first())?.let {
                        it.locations.add(locData)
                    }
                    Log.d("DB Status", "Location Updated to DB. Time : ${locData.time}, Latitude : ${locData.latitude}, Longitude : ${locData.longitude}")
                } else {
                    val loc = LocationInfo().apply {
                        this.userId = App.userId
                        this.locations = realmListOf(locData)
                    }

                    copyToRealm(loc)
                    Log.d("DB Status", "Entry Created for userId : ${App.userId}")
                }
            }
        }
    }

}

