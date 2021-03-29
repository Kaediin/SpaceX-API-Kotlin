package com.kaedin.api.models

import java.io.Serializable

data class Links(
    var youtube : String,
    var reddit : String,
    var news : String,
    var wikipedia : String,
    var spacex : String,
    var flickr_images : ArrayList<String>?,
    var mission_patch_small : String?
    ) : Serializable {
    constructor() : this("","","","","", null, null)
}