package com.swplanetapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@EntityScan
@SpringBootApplication
class SwPlanetApiApplication

fun main(args: Array<String>) {
	runApplication<SwPlanetApiApplication>(*args)
}
