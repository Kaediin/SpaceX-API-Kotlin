package com.kaedin.api.models

import java.io.Serializable

data class RocketMeasurements(
    var height: Double?,
    var diameter: Double?,
    var mass: Int?
): Serializable{
    constructor() : this(null, null, null)
}