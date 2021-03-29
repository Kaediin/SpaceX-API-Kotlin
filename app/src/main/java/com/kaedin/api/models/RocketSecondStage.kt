package com.kaedin.api.models

import RocketPayload
import java.io.Serializable

data class RocketSecondStage (
    var thrust: Int?,
    var reusable: Boolean?,
    var payloads: Payloads?,
    var engines: Int?,
    var fuel_amount_tons: Double?,
    var burn_time_sec: Int?
) : Serializable{
    constructor() : this(null, null, null, null, null, null)
}