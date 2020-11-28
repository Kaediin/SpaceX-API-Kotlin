package com.kaedin.api.models

import android.graphics.Bitmap
import java.io.Serializable

data class LaunchDetails(
    var id: Int,
    var details: String,
    var mission: String,
    var mission_patch_small: Bitmap?,
    var rocket: String,
    var launch_date: String,
    var launch_site: String,
    var launch_success : String,
    var links: Links?

) : Serializable {
    constructor() : this(
        -1,
        "",
        "",
        null,
        "",
        "",
        "",
        "",
        null
    )
}
