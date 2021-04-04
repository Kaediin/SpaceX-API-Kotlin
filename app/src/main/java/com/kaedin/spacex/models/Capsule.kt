package com.kaedin.spacex.models

data class Capsule(
    var reusedCount : Int?,
    var waterLandings : Int?,
    var landLandings : Int?,
    var lastUpdate : String?,
    var launches : ArrayList<String>?,
    var serial : String?,
    var status : String?,
    var type : String?,
    var id : String?
) {
    constructor() : this(null, null, null, null, null, null, null, null, "")
}