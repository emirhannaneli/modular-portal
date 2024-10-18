package dev.emirman.mp.core.config.docs

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DocsConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val server = Server()
            .url("http://mp.local.emirman.dev")
            .description("Local Server")

        val bearerScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name("Authorization")

        val security = SecurityRequirement()
            .addList("JWT Authentication")
        return OpenAPI()
            .info(info())
            .addServersItem(server)
            .addSecurityItem(security)
            .schemaRequirement("JWT Authentication", bearerScheme)
    }

    fun info(): Info {
        val contact = Contact()
            .email("info@emirman.dev")
            .url("https://emirman.dev")

        val licence = License()
            .name("Apache License Version 2.0")
            .url("https://www.apache.org/licenses/LICENSE-2.0")

        return Info()
            .title("Modular Portal Core")
            .description("Portal Core API")
            .version("v0.0.1-SNAPSHOT")
            .contact(contact)
            .license(licence)
    }
}