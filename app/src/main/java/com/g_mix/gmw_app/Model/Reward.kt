package com.g_mix.gmw_app.Model

class Reward {

    var id: Int? = null
    var name: String? = null
    var points: String? = null
    var description: String? = null
    var updated_at: String? = null
    var created_at: String? = null
    var image: String? = null

    constructor(
        id: Int?,
        name: String?,
        points: String?,
        description: String?,
        updated_at: String?,
        created_at: String?,
        image: String?
    ) {
        this.id = id
        this.name = name
        this.points = points
        this.description = description
        this.updated_at = updated_at
        this.created_at = created_at
        this.image = image
    }
}
