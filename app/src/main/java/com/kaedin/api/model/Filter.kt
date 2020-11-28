package com.kaedin.api.model

import java.io.Serializable

data class Filter (
    var showUpcoming : Boolean,
    var reverseList : Boolean,
    var lowQuality : Boolean
) : Serializable{
    constructor() : this(true, false, false)
}