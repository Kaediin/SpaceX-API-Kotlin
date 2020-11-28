package com.kaedin.api.models

import java.io.Serializable

data class Filter (
    var showUpcoming : Boolean,
    var reverseList : Boolean
) : Serializable{
    constructor() : this(true, false)
}