package com.kaedin.api.models

import java.io.Serializable

data class Fairings(
    var reused : Boolean?,
    var recovery_attempt : Boolean?,
    var recovered : Boolean?,
    var ship_ids : ArrayList<String>?
) : Serializable{
    constructor() : this(null, null, null, null)
}