package com.swplanetapi.extension

import com.swplanetapi.models.PlanetModel

fun PlanetModel.toPlanetModel() : PlanetModel {
    return PlanetModel(
        name = this.name,
        climate = this.climate,
        terrain = this.terrain
    )
}