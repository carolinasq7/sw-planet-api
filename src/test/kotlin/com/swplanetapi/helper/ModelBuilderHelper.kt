package com.swplanetapi.helper

import com.swplanetapi.models.PlanetModel
import net.bytebuddy.utility.RandomString
import java.util.UUID
import kotlin.random.Random

fun buildPlanet(
    id: Long = 0,
    name: String = UUID.randomUUID().toString(),
    climate: String = "Tropical",
    terrain: String = "Mountains, deserts, plains, plateaus, and other landforms"
) = PlanetModel (
    id = id,
    name = name,
    climate = climate,
    terrain = terrain
)

fun buildPlanetInvalid(
    id: Long = 0,
) = PlanetModel (
    id = id,
    name = "",
    climate = "",
    terrain = ""
)
