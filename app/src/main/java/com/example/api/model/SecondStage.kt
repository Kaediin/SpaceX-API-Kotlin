package com.example.api.model

import Payload
import java.io.Serializable

data class SecondStage (
    var payloads : ArrayList<Payload>?
) : Serializable{
    constructor() : this( null)
}