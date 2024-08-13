package com.swplanetapi.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.swplanetapi.repository.PlanetRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

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
        //TODO
    }

}