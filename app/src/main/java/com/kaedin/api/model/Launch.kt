package com.kaedin.api.model

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import java.io.Serializable

data class Launch (
    var id : Int,
    var details : String,
    var mission : String,
    var mission_patch_url: String?,
    var rocket : String,
    var upcoming : Boolean,
    var launch_date : String
    ) : Serializable {
    constructor() : this(-1, "", "", "", "",false, "")
}