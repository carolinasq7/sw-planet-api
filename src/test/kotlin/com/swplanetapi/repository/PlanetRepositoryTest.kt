package com.swplanetapi.repository

import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.helper.buildPlanetInvalid
import com.swplanetapi.service.PlanetService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

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
    fun `Create planet with valid data`() {
        val planet = buildPlanet()
        val savedPlanet = planetRepository.save(planet)
        val findPlanetCreated = planetRepository.findById(savedPlanet.id!!)

        assertNotNull(findPlanetCreated)
        assertEquals(planet.name, findPlanetCreated.get().name)
        assertEquals(planet.climate, findPlanetCreated.get().climate)
        assertEquals(planet.terrain, findPlanetCreated.get().terrain)
    }

    @Test
    fun `Should return incorrect request when invalid data is provided to create planet`() {
        val planetInvalid = buildPlanetInvalid()

        val exception = assertThrows<ResponseStatusException> {
            planetService.create(planetInvalid)
        }

        assertEquals(HttpStatus.BAD_REQUEST, exception.statusCode)
        assertEquals("Invalid data: All fields must be provided.", exception.reason)
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
