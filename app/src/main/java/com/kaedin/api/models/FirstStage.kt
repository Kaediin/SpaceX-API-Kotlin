package com.kaedin.api.models

import java.io.Serializable

data class FirstStage(
    var cores : ArrayList<Core>?


) : Serializable {
    constructor() : this(null)
}