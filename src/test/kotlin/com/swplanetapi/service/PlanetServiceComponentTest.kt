package com.swplanetapi.service

import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.repository.PlanetRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import org.junit.jupiter.api.assertThrows

@SpringBootTest
class PlanetServiceComponentTest {
    @Autowired
    private lateinit var planetService: PlanetService

    @Autowired
    private lateinit var planetRepository: PlanetRepository

    @BeforeEach
    fun setUp () {
        planetRepository.deleteAll()
    }

    @Test
    fun`Create planet with valid data`() {
        val planet = buildPlanet()
        val response = planetService.create(planet)

        assertEquals(response.statusCode, HttpStatus.CREATED)
    }

    @Test
    fun `Create planet with duplicate name`() {
        val planet = buildPlanet()
        planetRepository.save(planet)

        val duplicatePlanet = buildPlanet(name = planet.name)

        val exception = assertThrows<ResponseStatusException> {
            planetService.create(duplicatePlanet)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("Planet with name '${planet.name}' already exists.", exception.reason)
    }

}
