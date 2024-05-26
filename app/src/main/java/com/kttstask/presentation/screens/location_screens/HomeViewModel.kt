package com.kttstask.presentation.screens.location_screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kttstask.App
import com.kttstask.data.models.Location
import com.kttstask.data.models.LocationInfo
import com.kttstask.data.models.LoggedInUser
import com.kttstask.data.models.User
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    var users: MutableList<User> = mutableListOf()
        private set

    var historyList: List<Location> = listOf()
        private set

    fun getLocationHistory() {
        val locationHistory = App.realm.query<LocationInfo>(
            query = "userId == $0",
            args = arrayOf(App.userId)
        ).find()

        if (locationHistory.size > 0) {
            historyList = locationHistory.first().locations.toList().sortedBy { it.time }.asReversed()
        } else {
            historyList = emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getUsers() {
        users = App.realm.query<User>("isAuthenticated == $0", true).find().toMutableList()
        val user = users.filter { it._id == App.userId }[0]
        users.remove(user)
        users.add(0, user)
    }

    fun updateCurrentUserSession(user: User) {
        viewModelScope.launch {
            App.realm.write {
                val loggedInUser = App.realm.query<LoggedInUser>().find()
                findLatest(loggedInUser.first())?.let {
                    it.userId = user._id
                }
            }
        }
        App.userId = user._id
        users.remove(user)
        users.add(0, user)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun logoutUser(user: User) = viewModelScope.launch {
        App.realm.write {
            findLatest(user)?.let {
                it.isAuthenticated = false
            }
        }
    }

}