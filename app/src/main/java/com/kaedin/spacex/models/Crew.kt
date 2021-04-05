package com.kaedin.spacex.models

import java.io.Serializable

data class Crew(
    var id : String?,
    var name : String?,
    var agency : String?,
    var image : String?,
    var wikipedia : String?,
    var launchIds : ArrayList<String>?,
    var status : String?
) : Serializable {
    constructor() : this(null, null, null, null, null, null, null)
}