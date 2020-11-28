package com.kaedin.api.models

import Payload
import java.io.Serializable

data class SecondStage (
    var payloads : ArrayList<Payload>?
) : Serializable{
    constructor() : this( null)
}