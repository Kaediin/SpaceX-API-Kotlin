package com.example.api.model

import java.io.Serializable

data class Fairing(
    var reused : String,
    var recovery_attempt : String,
    var recovered : String,
    var ship : String
) : Serializable {
    constructor() : this("", "", "", "")
}