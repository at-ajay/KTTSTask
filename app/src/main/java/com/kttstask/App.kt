package com.kttstask

import android.app.Application
import com.kttstask.data.models.Location
import com.kttstask.data.models.LocationInfo
import com.kttstask.data.models.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class App: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()

        realm = Realm.open(
            configuration = RealmConfiguration.create(schema = setOf(User::class, Location::class, LocationInfo::class))
        )

    }

}