package com.example.api.model

import android.graphics.Bitmap
import java.io.Serializable
import kotlin.properties.Delegates

data class Launch (
    var id : Int,
    var details : String,
    var mission : String,
    var mission_patch_small : Bitmap?,
    var rocket : String,
    var upcoming : Boolean,
    var launch_date : String
    ) : Serializable {
    constructor() : this(-1, "", "", null, "",false, "")
}