package com.swplanetapi.service

import com.swplanetapi.models.PlanetModel
import com.swplanetapi.repository.PlanetRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PlanetService(
    private val planetRepository: PlanetRepository
) {
    fun create(planet: PlanetModel): ResponseEntity<PlanetModel> {
        if (planet.name.isBlank() || planet.climate.isBlank() || planet.terrain.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid data: All fields must be provided.")
        }

        if (planetRepository.existsByName(planet.name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Planet with name '${planet.name}' already exists.")
        }

        val savedPlanet = planetRepository.save(planet)
        return ResponseEntity(savedPlanet, HttpStatus.CREATED)
    }

    fun findById(id: Long): Optional<PlanetModel> {
        val planet = planetRepository.findById(id)
        if (planet.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Planet with id [${id}] not exists.")
        }
       return planet
    }

    fun getAllPlanets(name: String?, pageable: Pageable): Page<PlanetModel> {
        return if (name != null) {
            val planets = planetRepository.findByNameContaining(name, pageable)
            if (planets.isEmpty) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "No planets found with the name '$name'.")
            }
            planets
        } else {
            planetRepository.findAll(pageable)
        }
    }

    fun filterByClimateOrTerrain(climate: String?, terrain: String?, pageable: Pageable): Page<PlanetModel> {
        return if (climate != null && terrain != null) {
            val planets = planetRepository.findByClimateOrTerrain(climate, terrain, pageable)
            if (planets.isEmpty) {
                Page.empty(pageable)
            } else
            planets
        } else
            planetRepository.findAll(pageable)
    }

    fun delete(id: Long) {
        if (!planetRepository.existsById(id)){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Planet with id [${id}] not exists.")
        }
        planetRepository.deleteById(id)
    }
}