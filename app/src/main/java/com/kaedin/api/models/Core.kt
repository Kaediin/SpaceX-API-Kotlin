package com.kaedin.api.models

import java.io.Serializable

data class Core(
    var core_id : String,
    var flight : Int?,
    var gridfins : Boolean?,
    var legs : Boolean?,
    var reused : Boolean?,
    var landing_attempt : Boolean?,
    var landing_success : Boolean?,
    var landing_type : String?,
    var landpad : String?
) : Serializable{
    constructor() : this ("", null, null, null, null, null, null, null, null)
}
