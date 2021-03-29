package com.kaedin.api.models

import java.io.Serializable

data class CompositeFairing (
    var height : Double?,
    var diameter : Double?
) : Serializable {
    constructor() : this (null, null)
}