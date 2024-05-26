package com.kttstask.data.models

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.mongodb.kbson.ObjectId

class LocationInfo: RealmObject {

    @PrimaryKey var userId: ObjectId? = null
    var locations: RealmList<Location> = realmListOf()

}

@Parcelize
class Location: RealmObject, Parcelable {
    var time: Long = 0L
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}