package com.swplanetapi.service

import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.helper.buildPlanetInvalid
import com.swplanetapi.repository.PlanetRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.*
import kotlin.random.Random

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
        assertEquals(response.body, planet)
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
    fun `Should return ok and planet when id from a planet exists`() {
        val planet = buildPlanet()

        every { planetRepository.existsByName(planet.name) } returns true
        every { planetRepository.save(planet) } returns planet
        every { planetRepository.findById(planet.id) } returns Optional.of(planet)

        val responseGetId = planetService.findById(planet.id)

        assertEquals(planet, responseGetId.get())

    }

    @Test
    fun `Should return not found when id from a planet if not exists`() {
        val id = Random.nextInt().toLong()

        every { planetRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<ResponseStatusException>() {
            planetService.findById(id)
        }

        assertEquals(HttpStatus.NOT_FOUND, error.statusCode)
        assertEquals("Planet with id [${id}] not exists.", error.reason)
    }

    @Test
    fun `Should return ok and planet when a planet is filtered by an existing name`() {
        val planet = buildPlanet()
        val pageable = mockk<Pageable>()
        val planetsPage = PageImpl(listOf(planet))

        every { planetRepository.existsByName(planet.name) } returns true
        every { planetRepository.save(planet) } returns planet
        every { planetRepository.findByNameContaining(planet.name, pageable) } returns planetsPage

        val returnClimate = planetService.getAllPlanets(planet.name, pageable)
        val planetList = returnClimate.toList()

        assertEquals(1, planetList.size)
        assertEquals(planet.name, returnClimate.first().name)
        assertEquals(planet, returnClimate.first())

        verify { planetRepository.findByNameContaining(planet.name, pageable) }

    }

    @Test
    fun `Should return not found when name from a planet if not exists`() {
        val name = UUID.randomUUID().toString()
        val pageable = mockk<Pageable>()

        every { planetRepository.findByNameContaining(name, pageable) } returns Page.empty()

        val error = assertThrows<ResponseStatusException>() {
            planetService.getAllPlanets(name, pageable)
        }

        assertEquals(HttpStatus.NOT_FOUND, error.statusCode)
        assertEquals("No planets found with the name '$name'.", error.reason)
    }

    @Test
    fun `Should return ok and all planets when no filters are provided`() {
        val planet = buildPlanet()
        val planet1 = buildPlanet()
        val planet2 = buildPlanet()
        val pageable = mockk<Pageable>()
        val planetsPage = PageImpl(listOf(planet, planet1, planet2))

        every { planetRepository.findAll(pageable) } returns planetsPage

        val returnPlanets = planetService.getAllPlanets(null, pageable)
        val planetList = returnPlanets.toList()

        assertEquals(3, planetList.size)
        assertEquals(planet, planetList[0])
        assertEquals(planet1, planetList[1])
        assertEquals(planet2, planetList[2])

        verify { planetRepository.findAll(pageable) }

    }
}