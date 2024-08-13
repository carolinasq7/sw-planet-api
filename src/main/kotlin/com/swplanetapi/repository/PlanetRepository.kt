package com.swplanetapi.repository

import com.swplanetapi.models.PlanetModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PlanetRepository : JpaRepository<PlanetModel, Long> {
    fun existsByName(name: String): Boolean
    fun findByNameContaining(name: String?, pageable: Pageable): Page<PlanetModel>
    fun findByClimateOrTerrain(climate: String?, terrain: String?, pageable: Pageable): Page<PlanetModel>
}