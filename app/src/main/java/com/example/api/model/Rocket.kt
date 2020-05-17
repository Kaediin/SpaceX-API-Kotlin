package com.example.api.model

import java.io.Serializable

data class Rocket(
    var firstStage: FirstStage?,
    var secondStage: SecondStage?,
    var fairing: Fairing?
) : Serializable {
    constructor() : this(null, null, null)
}