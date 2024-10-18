import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("maven-publish")
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.emirman.mp.addon"
version = "1.0.2"

val coreVersion = "1.0.3"


publishing {
    repositories {
        val properties = Properties()
        properties.load(file("github.properties").inputStream())
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/emirhannaneli/modular-portal-notifier")
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

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencyManagement {
    imports {
        mavenBom("dev.emirman.mp:core:$coreVersion")
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

dependencies {
    implementation("org.simplejavamail:simple-java-mail:8.6.3")
    compileOnly("dev.emirman.mp:core:$coreVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks {
    shadowJar {
        archiveFileName = "${rootProject.name}-${rootProject.version}.jar"
    }
}