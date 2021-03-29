package com.kaedin.api.models

import java.io.Serializable

data class LandingLegs(
    var number : Int?,
    var material : String?
) : Serializable {
    constructor() : this(null ,null)
}