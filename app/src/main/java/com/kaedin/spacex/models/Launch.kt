package com.kaedin.spacex.models

import java.io.Serializable

data class Launch(
    var id: String,
    var name: String,
    var flight_number: Int?,
    var rocket_id: String?,
    var upcoming: Boolean?,
    var date_unix: Int?,
    var details: String?,
    var links: Links,
    var fairing: Fairing,
    var tbd: Boolean?,
    var net: Boolean?,
    var window: Int?,
    var success: Boolean?,
    var crew_ids: ArrayList<String>?,
    var ship_ids: ArrayList<String>?,
    var capsule_ids: ArrayList<String>?,
    var payload_ids: ArrayList<String>?,
    var launchpad_id: String?,
    var auto_update: Boolean?,
    var launch_library_id: String?,
    var failures: ArrayList<Failure>,
    var cores: ArrayList<String>?
    ) : Serializable {
    constructor() : this(
        "",
        "",
        null,
        null,
        null,
        null,
        null,
        Links(),
        Fairing(),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        ArrayList<Failure>(),
        null,
    )

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

    data class Fairing(
        var reused: Boolean?,
        var recovery_attempt: Boolean?,
        var recovered: Boolean?,
        var ship_ids: ArrayList<String>?
    ) : Serializable {
        constructor() : this(null, null, null, null)
    }

    class Failure(
        var time: Int?,
        var altitude: Int?,
        var reason: String?
    ) : Serializable {
        constructor() : this(null, null, null)
    }
}