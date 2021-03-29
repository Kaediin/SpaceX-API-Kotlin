package com.kaedin.api.models

import java.io.Serializable

data class Payloads (
    var compositeFairing : CompositeFairing?,
    var option_1 : String?
) : Serializable {
    constructor() : this (null, null)
}