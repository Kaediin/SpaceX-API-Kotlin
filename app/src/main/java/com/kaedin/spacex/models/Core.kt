package com.kaedin.spacex.models

import java.io.Serializable

data class Core(
    var block : Int?,
    var reuseCount : Int?,
    var rltsAttempts : Int?,
    var rltsLandings : Int?,
    var asdsAttempts : Int?,
    var asdsLandings : Int?,
    var lastUpdate : String?,
    var launchIds : ArrayList<String>?,
    var serial : String?,
    var status : String,
    var id : String
){
    constructor() : this(null, null, null, null, null, null, null, null, null, "", "")
}