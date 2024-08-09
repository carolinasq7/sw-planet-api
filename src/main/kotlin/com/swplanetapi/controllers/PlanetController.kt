package com.swplanetapi.controllers

import com.swplanetapi.controllers.request.PostPlanetRequest
import com.swplanetapi.models.PlanetModel
import com.swplanetapi.service.PlanetService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/planets")
class PlanetController(
    private val planetService: PlanetService
) {
    @PostMapping
    fun create(@RequestBody request: PostPlanetRequest) : ResponseEntity<PlanetModel> {
        val planet = PlanetModel(
            id = 0,
            name = request.name,
            climate = request.climate,
            terrain = request.terrain
        )
        planetService.create(planet)
        return ResponseEntity(planet, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<PlanetModel> {
        return planetService.findById(id).map { planet -> ResponseEntity.ok(planet)
        }.orElseGet{
            ResponseEntity.notFound().build()
        }
    }
}