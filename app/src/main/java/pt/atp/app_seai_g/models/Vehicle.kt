package pt.atp.app_seai_g.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

// [START blog_user_class]
@IgnoreExtraProperties
data class Vehicle(
    var uid: String? = "",
    var numCharges: Int = 0,
    var type: String? = "",
    var brand: String? = "",
    var model: String? = "",
) {
    @Exclude
    fun toMap(): Map<String,Any?>{
        return mapOf(
            "uid" to uid,
            "numCharges" to numCharges,
            "type" to type,
            "brand" to brand,
            "model" to model
        )
    }
}