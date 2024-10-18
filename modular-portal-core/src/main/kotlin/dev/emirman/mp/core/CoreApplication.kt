package dev.emirman.mp.core

import dev.emirman.mp.core.addon.v1.AddonManager
import net.lubble.util.config.utils.EnableLubbleUtils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableLubbleUtils
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaRepositories("dev.emirman.mp.core.repo.jpa")
@EnableMongoRepositories("dev.emirman.mp.core.repo.mongo")
@ConfigurationPropertiesScan("dev.emirman.mp.core.config.app")
class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args, init = {
        setInitializers(mutableListOf(AddonManager()))
    })
}
