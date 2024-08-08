package com.swplanetapi.controllers

import com.swplanetapi.controllers.request.PostPlanetRequest
import com.swplanetapi.models.PlanetModel
import com.swplanetapi.service.PlanetService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/planets")
class PlanetController(
    private val planetService: PlanetService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: PostPlanetRequest) {
        val planet = PlanetModel(
            id = 0,
            name = request.name,
            climate = request.climate,
            terrain = request.terrain
        )
        planetService.create(planet)
    }
}