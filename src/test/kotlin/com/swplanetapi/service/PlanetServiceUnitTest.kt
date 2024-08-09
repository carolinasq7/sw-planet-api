package com.swplanetapi.service

import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.helper.buildPlanetInvalid
import com.swplanetapi.models.PlanetModel
import com.swplanetapi.repository.PlanetRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.Optional

@ExtendWith(MockKExtension::class)
class PlanetServiceUnitTest {
    @MockK
    private lateinit var planetRepository: PlanetRepository

    @InjectMockKs
    private lateinit var planetService: PlanetService

    @Test
    fun `Should return created when data for a valid planet is entered`() {
        val planet = buildPlanet()

        every { planetRepository.existsByName(planet.name) } returns false
        every { planetRepository.save(planet) } returns planet

        val response = planetService.create(planet)

        assertEquals(response.statusCode, HttpStatus.CREATED)
    }

    @Test
    fun `Should return bad request when data for a invalid planet is entered`() {
        val planetInvalid = buildPlanetInvalid()

        every { planetRepository.existsByName(planetInvalid.name) } returns false
        every { planetRepository.save(planetInvalid) } throws RuntimeException("Invalid data")

        val exception = assertThrows<RuntimeException> {
            planetService.create(planetInvalid)
        }

        assertEquals("Invalid data", exception.message)
    }

    @Test
    fun `Should return ok and planet when data from a planet exists`() {
        val planet = buildPlanet()

        every { planetRepository.existsByName(planet.name) } returns true
        every { planetRepository.save(planet) } returns planet
        every { planetRepository.findById(planet.id) } returns Optional.of(planet)

        val responseGetId = planetService.findById(planet.id)

        assertEquals(planet, responseGetId.get())

    }

    @Test
    fun `Should return not found when data from a planet if not exists`() {
        val planet = buildPlanet()

        every { planetRepository.existsByName(planet.name) } returns true
        every { planetRepository.save(planet) } returns planet
        every { planetRepository.findById(planet.id) } returns Optional.empty()

        //TODO

    }

}