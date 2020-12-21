package pt.atp.app_seai_g.models

import com.google.firebase.database.IgnoreExtraProperties

// [START blog_user_class]
@IgnoreExtraProperties
data class Vehicle(
    var numCharges: Int = 0,
    var type: String? = "",
    var brand: String? = "",
    var model: String? = "",
)