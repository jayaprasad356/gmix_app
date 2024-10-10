package com.g_mix.gmw_app.Model

class CategoryItem {

    var id: Int? = null
    var categoryId: Int? = null
    var categoryName: String? = null
    var name: String? = null
    var unit: String? = null
    var measurement: String? = null
    var quantity: String? = null
    var description: String? = null
    var price: String? = null
    var image: String? = null
    var ratings: String? = null
    var updatedAt: String? = null
    var createdAt: String? = null

    constructor(
        id: Int?,
        categoryId: Int?,
        categoryName: String?,
        name: String?,
        unit: String?,
        measurement: String?,
        quantity: String?,
        description: String?,
        price: String?,
        image: String?,
        ratings: String?,
        updatedAt: String?,
        createdAt: String?,
    ) {
        this.id = id
        this.categoryId = categoryId
        this.categoryName = categoryName
        this.name = name
        this.unit = unit
        this.measurement = measurement
        this.quantity = quantity
        this.description = description
        this.price = price
        this.image = image
        this.ratings = ratings
        this.updatedAt = updatedAt
        this.createdAt = createdAt
    }
}