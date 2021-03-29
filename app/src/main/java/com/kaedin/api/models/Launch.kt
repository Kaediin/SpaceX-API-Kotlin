package com.kaedin.api.models

import java.io.Serializable

data class Launch (
    var id : String,
    var name : String,
    var flight_number : Int?,
    var rocket_id : String?,
    var upcoming : Boolean?,
    var date_unix : Int?,
    var details : String?,
    var links : Links?,
    var fairings : Fairings?,
    var tbd : Boolean?,
    var net : Boolean?,
    var window : Int?,
    var success : Boolean?,
    var crew_ids : ArrayList<String>?,
    var ship_ids : ArrayList<String>?,
    var capsule_ids : ArrayList<String>?,
    var payload_ids : ArrayList<String>?,
    var launchpad_id : String?,
    var auto_update : Boolean?,
    var launch_library_id : String?,
    var failures : ArrayList<Failure>?,
    var cores : Core?,
    ) : Serializable {
    constructor() : this("", "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,)
}