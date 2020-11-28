package com.kaedin.api.model

import java.io.Serializable

data class Core(
    var core_serial : String?,
    var flight : String?,
    var block : String?,
    var gridfins : String?,
    var legs : String?,
    var reused : String?,
    var land_success : String?,
    var landing_intent : String?,
    var landing_type : String?,
    var landing_vehicle : String?


) : Serializable {
    constructor() : this("", "", "", "", "", "", "", "", "", "")
}