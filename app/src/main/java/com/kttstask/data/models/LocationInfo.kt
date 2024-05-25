package com.kttstask.data.models

import com.google.android.gms.maps.model.LatLng
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class LocationInfo: RealmObject {

    @PrimaryKey var userId: ObjectId? = null
    var locations: RealmList<Location> = realmListOf()

}

class Location: RealmObject {
    var time: Long = 0L
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}