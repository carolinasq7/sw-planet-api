plugins {
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	id("org.flywaydb.flyway") version "9.19.0"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("plugin.noarg") version "1.9.24"
	jacoco
}

group = "com.carolcursos"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("jakarta.validation:jakarta.validation-api:3.0.0")
	implementation("org.hibernate.validator:hibernate-validator:6.2.0.Final")
	implementation("org.springframework.hateoas:spring-hateoas")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	runtimeOnly("com.mysql:mysql-connector-j")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.mockk:mockk:1.13.12")
	testImplementation("com.h2database:h2")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.9.3")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform {
		testLogging {
			events("passed", "skipped", "failed")
			exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
		}
	}
}

tasks.register<Test>("unitTest") {
	useJUnitPlatform {
		includeTags = setOf("unit")
	}
}

tasks.register<Test>("integrationTest") {
	useJUnitPlatform {
		includeTags = setOf("integration")
	}
}

tasks.register<Test>("componentTest") {
	useJUnitPlatform {
		includeTags = setOf("component")
	}
}

tasks.register<Test>("e2eTest") {
	useJUnitPlatform {
		includeTags = setOf("e2e")
	}
}