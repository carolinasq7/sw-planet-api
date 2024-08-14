package com.swplanetapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.helper.buildPlanetInvalid
import com.swplanetapi.repository.PlanetRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.server.ResponseStatusException

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
class PlanetControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var planetRepository: PlanetRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

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
}