import java.io.Serializable

data class RocketPayload (
    var payload_id : String?,
    var reused : String?,
    var customers : String?,
    var nationality : String?,
    var manufacturer : String?,
    var payload_type : String?,
    var payload_mass_kg : String?,
    var orbit : String?

) : Serializable {
    constructor() : this( "", "", "","", "", "", "", "")
}