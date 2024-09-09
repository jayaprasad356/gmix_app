package com.g_mix.gmw_app.Model

class HomeProduct {
    var id: String? = null
    var name: String? = null
    var unit: String? = null
    var measurement: String? = null
    var price: String? = null
    var image: String? = null
    var updated_at: String? = null
    var created_at: String? = null
    var description: String? = null
    var ratings: String? = null


    constructor(
        id: String?,
        name: String?,
        unit: String?,
        measurement: String?,
        price: String?,
        image: String?,
        updated_at: String?,
        created_at: String?,
        description: String?,
        ratings: String?

    ) {
        this.id = id
        this.name = name
        this.unit = unit
        this.measurement = measurement
        this.price = price
        this.image = image
        this.updated_at = updated_at
        this.created_at = created_at
        this.description = description
        this.ratings = ratings
    }
}
