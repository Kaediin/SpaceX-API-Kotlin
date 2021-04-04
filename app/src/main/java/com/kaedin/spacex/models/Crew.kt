package com.kaedin.spacex.models

import java.io.Serializable

data class Crew(
    var id : String?,
    var name : String?,
    var agency : String?,
    var image : String?,
    var wikipedia : String?,
    var launch_ids : ArrayList<String>?,
    var status : Boolean?
) : Serializable {
    constructor() : this(null, null, null, null, null, null, null)
}