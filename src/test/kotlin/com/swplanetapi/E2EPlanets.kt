package com.swplanetapi

import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.models.PlanetModel
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class E2EPlanets {
    @Autowired
    private lateinit var restTemplate : TestRestTemplate

    @Test
    fun `Create planet with success`() {
        val planet = buildPlanet()

        val response: ResponseEntity<PlanetModel> = restTemplate.exchange(
            "/planets",
            HttpMethod.POST,
            HttpEntity(planet),
            PlanetModel::class.java
        )
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(planet.name, response.body?.name)
        assertEquals(planet.climate, response.body?.climate)
        assertEquals(planet.terrain, response.body?.terrain)
    }
}