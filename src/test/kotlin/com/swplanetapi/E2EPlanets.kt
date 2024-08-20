package com.swplanetapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.swplanetapi.helper.buildPlanet
import com.swplanetapi.models.PlanetModel
import com.swplanetapi.repository.PlanetRepository
import org.junit.jupiter.api.BeforeEach
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class E2EPlanets {
    @Autowired
    private lateinit var restTemplate : TestRestTemplate

    @Autowired
    private lateinit var planetRepository: PlanetRepository

    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
    }

    @BeforeEach
    fun setup() = planetRepository.deleteAll()

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

    @Test
    fun `Get planet by ID return planet`() {
        val planet = planetRepository.save(buildPlanet())
        val id = planet.id

        val response: ResponseEntity<PlanetModel> = restTemplate.getForEntity(
            "/planets/${id}",
            PlanetModel::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(planet.name, response.body?.name)
        assertEquals(planet.climate, response.body?.climate)
        assertEquals(planet.terrain, response.body?.terrain)
    }

    @Test
    fun `Get all planets returns planet page`() {
        planetRepository.save(buildPlanet())

        val response: ResponseEntity<String> = restTemplate.getForEntity("/planets?", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val json = response.body ?: throw AssertionError("Response body is null")
        val verifyPage = objectMapper.readTree(json)
        val contentPage = verifyPage.get("content")

        assertTrue(contentPage.isArray)
        assertFalse(contentPage.isEmpty)
    }

    @Test
    fun `Get planet by name returns planet page`() {
        val planet = planetRepository.save(buildPlanet())
        val name = planet.name

        val response: ResponseEntity<String> = restTemplate.getForEntity("/planets?name=${name}", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val json = response.body ?: throw AssertionError("Response body is null")
        val verifyPage = objectMapper.readTree(json)
        val contentPage = verifyPage.get("content")

        assertTrue(contentPage.isArray)
        assertFalse(contentPage.isEmpty)

        val firstPlanet = contentPage[0]
        assertEquals(name, firstPlanet.get("name").asText())
        assertEquals(planet.climate, firstPlanet.get("climate").asText())
        assertEquals(planet.terrain, firstPlanet.get("terrain").asText())
    }

    @Test
    fun `Get planet by filter climate returns planet page`() {
        val planet = planetRepository.save(buildPlanet())
        val climate = planet.climate

        val response: ResponseEntity<String> = restTemplate.getForEntity("/planets?climate=${climate}", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val json = response.body ?: throw AssertionError("Response body is null")
        val verifyPage = objectMapper.readTree(json)
        val contentPage = verifyPage.get("content")

        assertTrue(contentPage.isArray)
        assertFalse(contentPage.isEmpty)

        val firstPlanet = contentPage[0]
        assertEquals(climate, firstPlanet.get("climate").asText())
        assertEquals(planet.name, firstPlanet.get("name").asText())
        assertEquals(planet.terrain, firstPlanet.get("terrain").asText())
    }

    @Test
    fun `Delete planet by ID`() {
        val planet = planetRepository.save(buildPlanet())
        val id = planet.id

        val response: ResponseEntity<Void> = restTemplate.exchange(
            "/planets/${id}",
            HttpMethod.DELETE,
            null,
            Void::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

    }
}