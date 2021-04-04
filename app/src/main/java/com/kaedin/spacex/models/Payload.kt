package com.kaedin.spacex.models

class Payload(
    var id : String,
    var name : String,
    var type : String,
    var reused : Boolean?,
    var launch : String,
    var customers : ArrayList<String>?,
    var norad_ids : ArrayList<String>?,
    var nationalities : ArrayList<String>?,
    var manufacturers : ArrayList<String>?,
    var mass : Int?,
    var orbit : String?,
    var referenceSystem : String?,
    var regime : String?,
    var semiMajorAxis : Int?,
    var eccentricity : Double?,
    var epoch : String?,
    var lifespanYears : Int?
) {
    constructor() : this("", "", "", null, "", null, null, null, null, null, null, null, null, null, null, null, null)
}