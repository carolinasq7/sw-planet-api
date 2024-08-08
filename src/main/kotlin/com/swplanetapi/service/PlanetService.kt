package com.swplanetapi.service

import com.swplanetapi.models.PlanetModel
import com.swplanetapi.repository.PlanetRepository
import org.springframework.stereotype.Service

@Service
class PlanetService(
    private val planetRepository: PlanetRepository
) {
   fun create(planet: PlanetModel) {
       planetRepository.save(planet)
   }

}