package com.swplanetapi.repository

import com.swplanetapi.models.PlanetModel
import org.springframework.data.repository.CrudRepository

interface PlanetRepository : CrudRepository<PlanetModel, Long>