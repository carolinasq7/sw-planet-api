package com.swplanetapi.controllers.request

import org.jetbrains.annotations.NotNull

data class PostPlanetRequest(
    @field:NotNull
    var name: String,

    @field:NotNull
    var climate: String,

    @field:NotNull
    var terrain: String
)