import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	id("io.kotest") version "0.3.9"
	id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
}


group = "com.rsq"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.session:spring-session-core")
	implementation("org.springframework:spring-core")
	implementation("org.springframework:spring-context")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")

	runtimeOnly("org.postgresql:postgresql:42.2.24")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.kotest:kotest-runner-junit5-jvm:5.4.2")
	testImplementation("io.kotest:kotest-assertions-core-jvm:5.4.2")
	testImplementation("io.kotest:kotest-property-jvm:5.4.2")
	testImplementation("io.kotest:kotest-runner-console-jvm:4.1.3")
	testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
	testImplementation("io.mockk:mockk:1.12.4")
	testImplementation("com.h2database:h2:2.1.214")
//	testImplementation("com.ninja-squad:springmockk:3.1.1")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

allOpen {
	annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}