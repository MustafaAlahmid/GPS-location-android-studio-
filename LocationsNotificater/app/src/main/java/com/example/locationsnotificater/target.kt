package com.example.locationsnotificater

import android.location.Location

class target{
    var name :String?=null
    var location:Location?=null
    var isNear:Boolean?=false

    constructor(name:String, lon:Double, lat:Double, isNear:Boolean){
        this.name = name
        this.isNear = isNear
        this.location = Location(name)
        this.location!!.longitude = lon
        this.location!!.latitude = lat

    }
}