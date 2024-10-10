package com.g_mix.gmw_app.Model

class SliderItem {

    var id: Int? = null
    var name: String? = null
    var link: String? = null
    var image: String? = null
    var updatedAt: String? = null
    var createdAt: String? = null

    constructor(
        id: Int?,
        name: String?,
        link: String?,
        image: String?,
        updatedAt: String?,
        createdAt: String?,
    ) {
        this.id = id
        this.name = name
        this.link = link
        this.image = image
        this.updatedAt = updatedAt
        this.createdAt = createdAt
    }
}
