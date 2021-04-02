package com.kaedin.api.models

import java.io.Serializable

data class Rocket(
    var id: String,
    var name: String?,
    var measurements: RocketMeasurements,
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
    var rocketFirstStage: RocketFirstStage,
    var rocketSecondStage: RocketSecondStage,
    var engine: Engine,
    var landingLegs: LandingLegs
) : Serializable {
    constructor() : this(
        "",
        null,
        RocketMeasurements(),
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
        RocketFirstStage(),
        RocketSecondStage(),
        Engine(),
        LandingLegs()
    )

    data class RocketMeasurements(
        var height: Double?,
        var diameter: Double?,
        var mass: Int?
    ) : Serializable {
        constructor() : this(null, null, null)
    }

    data class RocketFirstStage(
        var thrust_sea_level: Int?,
        var thrust_vacuum: Int?,
        var reusable: Boolean?,
        var engines: Int?,
        var fuel_amount_tons: Double?,
        var burn_time_sec: Int?
    ) : Serializable {
        constructor() : this(null, null, null, null, null, null)
    }

    data class RocketSecondStage(
        var thrust: Int?,
        var reusable: Boolean?,
        var payload: Payload,
        var engines: Int?,
        var fuel_amount_tons: Double?,
        var burn_time_sec: Int?
    ) : Serializable {
        constructor() : this(null, null, Payload(), null, null, null)

        data class Payload(
            var compositeFairing: CompositeFairing,
            var option_1: String?
        ) : Serializable {
            constructor() : this(CompositeFairing(), null)

            data class CompositeFairing(
                var height: Double?,
                var diameter: Double?
            ) : Serializable {
                constructor() : this(null, null)
            }

        }
    }

    data class Engine(
        var isp: ISP,
        var thrust_sea_level: Double?,
        var thrust_vacuum: Double?,
        var number: Int?,
        var type: String?,
        var version: String?,
        var layout: String?,
        var engine_loss_max: Int?,
        var propellant_1: String?,
        var propellant_2: String?,
        var thrust_to_weight: Double?
    ) : Serializable {
        constructor() : this(ISP(), null, null, null, null, null, null, null, null, null, null)

        data class ISP(
            var sea_level: Int?,
            var vacuum: Int?
        ) : Serializable {
            constructor() : this(null, null)
        }
    }

    data class LandingLegs(
        var number: Int?,
        var material: String?
    ) : Serializable {
        constructor() : this(null, null)
    }
}