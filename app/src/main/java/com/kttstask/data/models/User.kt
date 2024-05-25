package com.kttstask.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User: RealmObject {

    @PrimaryKey var _id: ObjectId = ObjectId()
    var fullName: String = ""
    var emailAddress: String = ""
    var password: String = ""

}