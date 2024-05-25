package com.kttstask.presentation.screens.auth_screens

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kttstask.App
import com.kttstask.data.models.User
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    fun createUser(name: String, email: String, password: String) = viewModelScope.launch {
        App.realm.write {
            val user = User().apply {
                this.fullName = name
                this.emailAddress = email
                this.password = password
            }

            copyToRealm(user)
        }
    }

    fun canLoginUser(email: String, password: String): Boolean {
        val userSearchResult = App.realm.query<User>(
            query = "emailAddress == $0 && password == $1",
            args = arrayOf(email, password)
        ).find()

        return userSearchResult.size > 0
    }

    fun isEmailExists(email: String): Boolean {
        val emailSearchResult = App.realm.query<User>(
            query = "emailAddress == $0",
            args = arrayOf(email)
        ).find()

        return emailSearchResult.size > 0
    }

    fun updateUserPassword(email: String, password: String): Boolean {
        val user = App.realm.query<User>(query = "emailAddress == $0", args = arrayOf(email)).find().first()
        if (user.password == password) {
            return false
        }

        viewModelScope.launch {
            App.realm.write {
                findLatest(user)?.let {
                    it.password = password
                }
            }
        }

        return true
    }

}