package dev.emirman.mp

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.*

class CorePlugin : Plugin<Project> {
    private val coreVersion = "1.0.3"

    override fun apply(project: Project) {
        project.plugins.apply("org.jetbrains.kotlin.jvm")
        project.plugins.apply("org.jetbrains.kotlin.plugin.spring")
        project.plugins.apply("com.gradle.plugin-publish")
        project.plugins.apply("io.spring.dependency-management")

        project.repositories.maven { repo ->
            repo.url = project.uri("https://repo.emirman.dev/repository/maven-public/")
        }

        project.repositories.maven { repo ->
            repo.name = "GitHubPackages"
            repo.url = project.uri("https://maven.pkg.github.com/emirhannaneli/modular-portal-core")

            val githubProps = project.file("github.properties").inputStream().use { input ->
                Properties().apply { load(input) }
            }

            repo.credentials {
                it.username = githubProps["gpr.user"] as String
                it.password = githubProps["gpr.key"] as String
            }
        }

        project.dependencies.apply {
            add("compileOnly", "dev.emirman.mp:core:$coreVersion")
        }
    }
}