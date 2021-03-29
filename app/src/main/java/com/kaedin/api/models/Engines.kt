package com.kaedin.api.models

import java.io.Serializable

data class Engines(
    var isp : ISP?,
    var thrust_sea_level: Double?,
    var thrust_vacuum : Double?,
    var number : Int?,
    var type : String?,
    var version : String?,
    var layout : String?,
    var engine_loss_max : Int?,
    var propellant_1 : String?,
    var propellant_2 : String?,
    var thrust_to_weight : Double?
) : Serializable {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)
}