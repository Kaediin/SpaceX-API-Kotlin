package com.kaedin.api.models

import java.io.Serializable

data class Rocket(
    var firstStage: FirstStage?,
    var secondStage: SecondStage?,
    var fairing: Fairing?
) : Serializable {
    constructor() : this(null, null, null)
}