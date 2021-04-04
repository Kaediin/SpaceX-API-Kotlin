package com.kaedin.spacex.models

import java.io.Serializable

data class Dragon(
    var heatShield: HeatShield?,
    var launchPayloadMass: Int?,
    var launchPayloadVolume: Int?,
    var returnPayloadMass: Int?,
    var returnPayloadVolume: Int?,
    var trunk: Trunk?,
    var height : Double?,
    var diameter: Double?,
    var firstFlight: String?,
    var flickrImages: ArrayList<String>?,
    var name: String?,
    var type: String?,
    var active: Boolean?,
    var crewCapacity: Int?,
    var orbitDurationYears: Int?,
    var thursters: ArrayList<Thruster>?,
    var wikipedia: String?,
    var description: String?,
    var id: String

) : Serializable {

    data class Trunk(
        var volumeCubicMeters: Int?,
        var solarArray: Int?,
        var unpressurizedCargo: Boolean?
    ) : Serializable {
        constructor() : this(null, null, null)
    }

    data class Thruster(
        var type: String,
        var amount: Int?,
        var pods: Int?,
        var fuel_1: String?,
        var fuel_2: String?,
        var isp: Int?,
        var thrustkN: Double?
    ) : Serializable {
        constructor() : this("", null, null, null, null, null, null)
    }

    data class HeatShield(
        var material: String?,
        var sizeMeters: Double?,
        var tempDegrees: Double?,
        var developmentPartner: String?
    ) : Serializable {
        constructor() : this(null, null, null, null)
    }

    constructor() : this(
        HeatShield(),
        null,
        null,
        null,
        null,
        Trunk(),
        null,
        null,
        null,
        ArrayList<String>(),
        null,
        null,
        null,
        null,
        null,
        ArrayList<Thruster>(),
        null,
        null,
        ""
    )
}