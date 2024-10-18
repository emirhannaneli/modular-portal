import java.net.URI
import java.util.*

plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.emirman.mp.addon"
version = "1.0.0"

val coreVersion = "1.0.3"

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
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    compileOnly("dev.emirman.mp:core:$coreVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation(kotlin("script-runtime"))
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
