import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("maven-publish")
    id("com.gradle.plugin-publish") version "1.2.1"
    kotlin("jvm") version "1.9.22"
}

group = "dev.emirman.mp"
version = "1.0.0-SNAPSHOT"

publishing {
    repositories {
        val properties = Properties()
        properties.load(file("github.properties").inputStream())
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/emirhannaneli/modular-portal-core-plugin")
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

gradlePlugin {
    website = "https://mp.emirman.dev"
    vcsUrl = "https://github.com/emirhannaneli/modular-portal-core-plugin"
    plugins {
        create("dev.emirman.mp") {
            id = "dev.emirman.mp"
            implementationClass = "dev.emirman.mp.CorePlugin"
            displayName = "Modular Portal Core"
            description = "Core plugin for Modular Portal"
            tags = listOf("modular", "portal", "core")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}