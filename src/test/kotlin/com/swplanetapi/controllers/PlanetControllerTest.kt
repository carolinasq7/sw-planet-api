package com.swplanetapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.helper.buildPlanetInvalid
import com.swplanetapi.repository.PlanetRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@Tag("integration")
class PlanetControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var planetRepository: PlanetRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        planetRepository.deleteAll()
    }

    @Test
    fun `Should return created when valid data is provided`() {
        val planet = buildPlanet()

        mockMvc.perform(
            post("/planets")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(planet))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(planet.name))
            .andExpect(jsonPath("$.climate").value(planet.climate))
            .andExpect(jsonPath("$.terrain").value(planet.terrain))

    }

    @Test
    fun `Should return bad request when invalid data is provided`() {
        val planetInvalid = buildPlanetInvalid()

        val result = mockMvc.perform(
            post("/planets")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(planetInvalid))
        )
            .andExpect(status().isBadRequest)
            .andReturn()

        val resolvedException = result.resolvedException as ResponseStatusException
        assertEquals("Invalid data: All fields must be provided.", resolvedException.reason)
    }

    @Test
    fun `Should return an bad request when the planet already exists`() {
        val planet = planetRepository.save(buildPlanet())
        val duplicatePlanet = buildPlanet(name = planet.name)

        val result = mockMvc.perform(
            post("/planets")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(duplicatePlanet))
        )
            .andExpect(status().isBadRequest)
            .andReturn()

        val resolvedException = result.resolvedException as ResponseStatusException
        assertEquals("Planet with name '${planet.name}' already exists.", resolvedException.reason)
    }

    @Test
    fun `Should return ok and planet when id from a planet exists`() {
        val planet = planetRepository.save(buildPlanet(id = 20))
        val id = planet.id

        mockMvc.perform(get("/planets/${id}").contentType("application/json"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(planet.name))
            .andExpect(jsonPath("$.climate").value(planet.climate))
            .andExpect(jsonPath("$.terrain").value(planet.terrain))

    }

    @Test
    fun `Should return not found when id from a planet if not exists`() {
        val id = Random.nextInt().toLong()

        val result = mockMvc.perform(get("/planets/${id}").contentType("application/json"))
            .andExpect(status().isNotFound)
            .andReturn()

        val exception = result.resolvedException as ResponseStatusException
        assertEquals("Planet with id [${id}] not exists.", exception.reason)
    }

    @Test
    fun `Should return ok and planet when name from a planet exists`() {
        val planet = planetRepository.save(buildPlanet(name = "test name"))

        mockMvc.perform(get("/planets?name=test name"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].name").value(planet.name))
            .andExpect(jsonPath("$.content[0].climate").value(planet.climate))
            .andExpect(jsonPath("$.content[0].terrain").value(planet.terrain))
    }

    @Test
    fun `Should return bad request when there is no planet with the name entered`() {

        val result = mockMvc.perform(get("/planets?name=Invalid"))
            .andExpect(status().isNotFound)
            .andReturn()

        val resolvedException = result.resolvedException as ResponseStatusException
        assertEquals("No planets found with the name 'Invalid'.", resolvedException.reason)
    }

    @Test
    fun `Should return ok and all planets when no filters are provided`() {

        planetRepository.save(buildPlanet())

        mockMvc.perform(get("/planets/filter?"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
    }

    @Test
    fun `Should return ok and planets when filters are provided`() {

        val planet = planetRepository.save(buildPlanet(climate = "test climate", terrain = "test terrain"))

        mockMvc.perform(get("/planets/filter?climate=test climate&terrain=test terrain"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].climate").value(planet.climate))
            .andExpect(jsonPath("$.content[0].terrain").value(planet.terrain))
    }

    @Test
    fun `Should return ok and an empty list when no matching planets are found for the given filters`() {

        mockMvc.perform(get("/planets/filter?climate=xpto"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `Should delete a planet when it exists`() {
        val planet = planetRepository.save(buildPlanet(id = 1L))
        val id = planet.id

        mockMvc.perform(delete("/planets/${id}"))
            .andExpect(status().isOk)
    }

    @Test
    fun `Should throw not found exception when trying to delete a non-existent planet`() {
        val id = Random.nextInt().toLong()

        val result = mockMvc.perform(delete("/planets/${id}"))
            .andExpect(status().isNotFound)
            .andReturn()

        val resolvedException = result.resolvedException as ResponseStatusException
        assertEquals("Planet with id [${id}] not exists.", resolvedException.reason)
    }
}