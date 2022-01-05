import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id ("org.jetbrains.kotlin.jvm") version "1.6.10-RC"
	kotlin("plugin.spring") version "1.5.31"
}

group = "com.tobybook"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("mysql:mysql-connector-java")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework:spring-jdbc:5.3.12")
	implementation("javax.mail:mail:1.4.7")
	implementation("org.springframework.boot:spring-boot-starter-mail:2.6.1")

	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
	runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
	runtimeOnly("org.aspectj:aspectjweaver:1.9.7")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
