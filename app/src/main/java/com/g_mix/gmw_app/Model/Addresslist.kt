package com.g_mix.gmw_app.Model

class Addresslist {

    var id: Int? = null
    var user_id: Int? = null
    var first_name: String? = null
    var last_name: String? = null
    var mobile: String? = null
    var alternate_mobile: String? = null
    var door_no: String? = null
    var street_name: String? = null
    var city: String? = null
    var pincode: String? = null
    var state: String? = null
    var landmark: String? = null
    var updated_at: String? = null
    var created_at: String? = null

    constructor(
        id: Int?,
        user_id: Int?,
        first_name: String?,
        last_name: String?,
        mobile: String?,
        alternate_mobile: String?,
        door_no: String?,
        street_name: String?,
        city: String?,
        pincode: String?,
        state: String?,
        landmark: String?,
        updated_at: String?,
        created_at: String?
    ) {
        this.id = id
        this.user_id = user_id
        this.first_name = first_name
        this.last_name = last_name
        this.mobile = mobile
        this.alternate_mobile = alternate_mobile
        this.door_no = door_no
        this.street_name = street_name
        this.city = city
        this.pincode = pincode
        this.state = state
        this.landmark = landmark
        this.updated_at = updated_at
        this.created_at = created_at
    }
}
