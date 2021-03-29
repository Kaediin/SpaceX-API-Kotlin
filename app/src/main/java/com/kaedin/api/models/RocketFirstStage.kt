package com.kaedin.api.models

import java.io.DataOutput
import java.io.Serializable

data class RocketFirstStage(
    var thrust_sea_level: Int?,
    var thrust_vacuum: Int?,
    var reusable: Boolean?,
    var engines: Int?,
    var fuel_amount_tons: Double?,
    var burn_time_sec: Int?
) : Serializable {
    constructor() : this(null,null,null,null,null, null)
}