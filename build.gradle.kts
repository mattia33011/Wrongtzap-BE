plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("kapt") version "2.1.10"
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
	implementation ("cn.hutool:hutool-core:5.8.20")

	//Kotlin Starter
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.arrow-kt:arrow-core:1.2.0")

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

	//Databases
	runtimeOnly("mysql:mysql-connector-java:8.0.33")
	implementation("io.minio:minio:8.5.14")

	// DGS Framework for GraphQL
	implementation("com.netflix.graphql.dgs:graphql-dgs-spring-graphql-starter")
	implementation("com.graphql-java:graphql-java-extended-scalars:22.0")


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

	//Metamodels
	compileOnly("org.hibernate:hibernate-jpamodelgen:6.4.4.Final")
	kapt("org.hibernate:hibernate-jpamodelgen:6.4.4.Final")

}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
