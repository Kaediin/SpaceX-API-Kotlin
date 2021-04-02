package com.kaedin.api.models

data class Landpad(
    var id: String,
    var name: String,
    var fullName: String,
    var type: String?,
    var locality: String?,
    var region: String?,
    var latitude: Double?,
    var longitude: Double?,
    var landingAttempts: Int?,
    var landingSuccesses: Int?,
    var wikipedia: String?,
    var details: String?,
    var launchIds: ArrayList<String>?,
    var status: String?
) {
    constructor() : this(
        "",
        "",
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}