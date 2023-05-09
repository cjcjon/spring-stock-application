import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.8"
  id("io.spring.dependency-management") version "1.0.15.RELEASE"
  id("org.jlleitschuh.gradle.ktlint") version "11.3.1"

  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21"
  kotlin("plugin.jpa") version "1.6.21"
}

group = "project"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
  mavenCentral()
}

object Versions {
  const val KOTEST = "5.5.4"
  const val KOTEST_SPRING = "1.1.2"
  const val MOCKK = "1.13.4"
  const val SPRING_MOCKK = "4.0.2"
  const val SPRING_CLOUD = "2021.0.7"
}

dependencies {
  // spring
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("com.h2database:h2")

  // kotlin
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  // development
  developmentOnly("org.springframework.boot:spring-boot-devtools")

  // logging
  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

  // test: spring
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "mockito-core")
  }
  testImplementation("org.springframework.security:spring-security-test")

  // test: kotest
  testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
  testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
  testImplementation("io.kotest:kotest-property:${Versions.KOTEST}")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:${Versions.KOTEST_SPRING}")

  // test: mockk
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("io.mockk:mockk:${Versions.MOCKK}")
  testImplementation("com.ninja-squad:springmockk:${Versions.SPRING_MOCKK}")
}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Versions.SPRING_CLOUD}")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "17"
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}
