import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("maven-publish")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.jpa") version "1.9.22"
}

group = "dev.emirman.mp"
version = "1.0.6-SNAPSHOT"

publishing {
    repositories {
        val properties = Properties()
        properties.load(file("github.properties").inputStream())
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/emirhannaneli/modular-portal-core")
            credentials {
                username = properties.getProperty("gpr.user")
                password = properties.getProperty("gpr.key")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            if (version.toString().endsWith("-SNAPSHOT")) {
                val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                version = "$version-$timestamp"
            }

            from(components["java"])

            artifact(tasks.jar) {
                classifier = "api"
            }
            artifact(tasks.getByName("kotlinSourcesJar"))
        }
    }
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    apiElements {
        extendsFrom(configurations.implementation.get())
    }
}

repositories {
    mavenCentral()

    maven {
        url = uri("https://repo.emirman.dev/repository/maven-public/")
    }

    val githubProps = Properties()
    githubProps.load(file("github.properties").inputStream())

    maven {
        name = "GitHubPackages"
        url = URI("https://maven.pkg.github.com/emirhannaneli/modular-portal-core")
        credentials {
            username = githubProps["gpr.user"].toString()
            password = githubProps["gpr.key"].toString()
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}

dependencies {
    // Joda Time
    implementation("joda-time:joda-time:2.12.7")
    // Reflections
    implementation("org.reflections:reflections:0.10.2")
    // Lubble
    implementation("net.lubble:microservice-utils:1.11.3")
    // Google
    implementation("com.google.guava:guava:33.0.0-jre")
    // Reactor
    implementation("io.projectreactor:reactor-core:3.6.3")
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Postgres
    runtimeOnly("org.postgresql:postgresql")
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.2.4")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Spring Boot Annotations
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.h2database:h2")
}

tasks.register("place-signature") {
    doFirst {
        signature()
    }
}

fun signature() {
    val banner = file("build/resources/main/banner.txt")
    var content = banner.readText()

    val name = rootProject.name
    val version = rootProject.version.toString()

    content = content.replace("{{name}}", name)
    content = content.replace("{{version}}", version)
    content = content.replace("{{description}}", "Coded with ❤\uFE0F")
    content = content.replace("{{year}}", Calendar.getInstance().get(Calendar.YEAR).toString())
    content = content.replace("{{author}}", "Emirhan Naneli")

    banner.writeText(content)
}

tasks.named("build") {
    finalizedBy("place-signature")
}

tasks {
    bootJar {
        archiveFileName = "${rootProject.name}.jar"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
