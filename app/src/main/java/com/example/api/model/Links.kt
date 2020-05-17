package com.example.api.model

import java.io.Serializable

data class Links(
    var youtube : String,
    var reddit : String,
    var news : String,
    var wikipedia : String,
    var spacex : String
) : Serializable {
    constructor() : this("","","","","")
}