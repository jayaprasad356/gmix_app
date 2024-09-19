package com.g_mix.gmw_app.Model

class OrderData {
    var id: Int? = null
    var user_name: String? = null
    var address_name: String? = null
    var product_name: String? = null
    var unit: String? = null
    var measurement: String? = null
    var quantity: String? = null
    var delivery_charges: String? = null
    var payment_mode: String? = null
    var price: String? = null
    var status: String? = null
    var est_delivery_date: String? = null
    var ordered_date: String? = null
    var updated_at: String? = null
    var created_at: String? = null
    var status_color: String? = null
    var live_tracking: String? = null
    var total_price: String? = null
    var ratings: String? = null

    constructor(
        id: Int?,
        user_name: String?,
        address_name: String?,
        product_name: String?,
        unit: String?,
        measurement: String?,
        quantity: String?,
        delivery_charges: String?,
        payment_mode: String?,
        price: String?,
        status: String?,
        est_delivery_date: String?,
        ordered_date: String?,
        updated_at: String?,
        created_at: String?,
        status_color: String?,
        live_tracking: String?,
        total_price: String?,
        ratings: String?
    ) {
        this.id = id
        this.user_name = user_name
        this.address_name = address_name
        this.product_name = product_name
        this.unit = unit
        this.measurement = measurement
        this.quantity = quantity
        this.delivery_charges = delivery_charges
        this.payment_mode = payment_mode
        this.price = price
        this.status = status
        this.est_delivery_date = est_delivery_date
        this.ordered_date = ordered_date
        this.updated_at = updated_at
        this.created_at = created_at
        this.status_color = status_color
        this.live_tracking = live_tracking
        this.total_price = total_price
        this.ratings = ratings
    }
}
