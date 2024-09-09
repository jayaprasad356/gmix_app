package com.g_mix.gmw_app.Model

class Review {

    var id: Int? = null
    var product_id: Int? = null
    var description: String? = null
    var image1: String? = null
    var image2: String? = null
    var image3: String? = null
    var ratings: String? = null

    constructor(
        id: Int?,
        product_id: Int?,
        description: String?,
        image1: String?,
        image2: String?,
        image3: String?,
        ratings: String?
    ) {
        this.id = id
        this.product_id = product_id
        this.description = description
        this.image1 = image1
        this.image2 = image2
        this.image3 = image3
        this.ratings = ratings

    }
}