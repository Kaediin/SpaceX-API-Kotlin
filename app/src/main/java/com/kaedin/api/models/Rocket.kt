package com.kaedin.api.models

import java.io.Serializable

data class Rocket(
    var id: String,
    var name: String?,
    var measurements: RocketMeasurements?,
    var type: String?,
    var active: Boolean?,
    var stages: Int?,
    var boosters: Int?,
    var cost_per_launch: Int?,
    var success_rate_pct: Double?,
    var first_flight: String?,
    var country: String?,
    var company: String?,
    var wikipedia_link: String?,
    var description: String?,
    var flickr_images: ArrayList<String>?,
    var rocketFirstStage: RocketFirstStage?,
    var rocketSecondStage: RocketSecondStage?,
    var engines: Engines?,
    var landingLegs: LandingLegs?
) : Serializable {
    constructor() : this(
        "",
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
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}