package com.kaedin.api.models

import java.io.Serializable

data class ISP(
    var sea_level : Int?,
    var vacuum : Int?
) : Serializable {
    constructor() : this(null, null)
}