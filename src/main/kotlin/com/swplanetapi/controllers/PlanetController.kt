package com.swplanetapi.controllers

import com.swplanetapi.controllers.request.PostPlanetRequest
import com.swplanetapi.extension.toPlanetModel
import com.swplanetapi.models.PlanetModel
import com.swplanetapi.service.PlanetService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @GetMapping()
    fun getAllPlanets(
        @RequestParam(name = "name", required = false) name: String?,
        @PageableDefault(page= 0, size = 10) pageable: Pageable
    ): Page<PlanetModel> {
        return planetService.getAllPlanets(name, pageable).map { it.toPlanetModel() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<PlanetModel> {
        return planetService.findById(id).map { planet -> ResponseEntity.ok(planet)
        }.orElseGet{
            ResponseEntity.notFound().build()
        }
    }

}