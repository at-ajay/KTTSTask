package com.kttstask

import android.app.Application
import android.preference.PreferenceDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kttstask.data.models.Location
import com.kttstask.data.models.LocationInfo
import com.kttstask.data.models.LoggedInUser
import com.kttstask.data.models.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.mongodb.kbson.ObjectId

class App: Application() {

    companion object {
        lateinit var realm: Realm
        lateinit var userId: ObjectId
    }

    override fun onCreate() {
        super.onCreate()

        realm = Realm.open(
            configuration = RealmConfiguration.create(schema = setOf(User::class, LoggedInUser::class, Location::class, LocationInfo::class))
        )

        val loggedInUser = realm.query<LoggedInUser>().find()
        if (loggedInUser.size > 0) {
            userId = loggedInUser.first().userId!!
        }

    }

}