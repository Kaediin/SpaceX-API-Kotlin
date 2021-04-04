package com.kaedin.spacex.models

data class Ship(
    var legacyId : String?,
    var id : String,
    var name : String,
    var active : Boolean?,
    var model : String?,
    var type : String?,
    var roles : ArrayList<String>?,
    var imo : Int?,
    var mmsi : Int?,
    var abs : Int?,
    var shipClass : Int?,
    var mass : Int?,
    var buildYear : Int?,
    var homePort : String?,
    var status : String?,
    var speedKN : Int?,
    var latitude : Double?,
    var longitude : Double?,
    var lastAisUpdate : String?,
    var marineTrafficLink : String?,
    var image : String?,
    var launches : ArrayList<String>?
) {
    constructor() : this(
        null,
        "",
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}