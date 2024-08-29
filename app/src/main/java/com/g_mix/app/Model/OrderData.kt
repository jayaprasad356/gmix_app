package com.g_mix.app.Model

class OrderData {
    var id: Int? = null
    var user_name: String? = null
    var address_name: String? = null
    var product_name: String? = null
    var delivery_charges: String? = null
    var payment_mode: String? = null
    var price: String? = null
    var place_status: String? = null
    var delivery_date: String? = null
    var updated_at: String? = null
    var created_at: String? = null


    constructor(
        id: Int?,
        user_name: String?,
        address_name: String?,
        product_name: String?,
        delivery_charges: String?,
        payment_mode: String?,
        price: String?,
        place_status: String?,
        delivery_date: String?,
        updated_at: String?,
        created_at: String?,

        ) {
        this.id = id
        this.user_name = user_name
        this.address_name = address_name
        this.product_name = product_name
        this.delivery_charges = delivery_charges
        this.payment_mode = payment_mode
        this.price = price
        this.place_status = place_status
        this.delivery_date = delivery_date
        this.updated_at = updated_at
        this.created_at = created_at
    }
}
