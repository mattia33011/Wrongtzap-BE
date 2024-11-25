plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
}

group = "it.mag"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:latest.release")
	}
}

dependencies {

	//Spring Starter
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-security:3.2.0")
	implementation("org.springframework.boot:spring-boot-starter-web:3.2.0")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.2.0")
	implementation ("org.springframework.security:spring-security-oauth2-jose")
	implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	testImplementation("org.springframework.boot:spring-boot-starter-test")


	//Kotlin Starter
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// Spring Test
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.2")
	testImplementation("io.mockk:mockk:1.13.12")
	testImplementation ("org.testcontainers:junit-jupiter")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// Test Container
	testImplementation("org.testcontainers:mysql:1.20.0")
	testImplementation ("org.springframework.boot:spring-boot-testcontainers")

	//MySQL
	runtimeOnly("com.mysql:mysql-connector-j")

	// DGS Framework for GraphQL
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")

	// JWT Security
	implementation ("com.auth0:java-jwt:4.4.0")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")

	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

	implementation("io.reactivex.rxjava3:rxjava:3.1.5")
	implementation ("org.springframework:spring-messaging")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
