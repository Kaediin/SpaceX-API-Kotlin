package com.kaedin.api.models

import java.io.Serializable

class Failure(
    var time : Int?,
    var altitude : Int?,
    var reason : String?
) : Serializable{
    constructor() : this(null, null, null)
}