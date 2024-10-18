package dev.emirman.mp.core.addon.v1

import dev.emirman.mp.core.CoreApplication
import dev.emirman.mp.core.model.addon.v1.Addon
import org.hibernate.jpa.HibernatePersistenceProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.Environment
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.util.ClassUtils
import java.io.File
import java.net.URLClassLoader
import javax.sql.DataSource

class AddonManager : ApplicationContextInitializer<ConfigurableApplicationContext> {
    private val logger: Logger = LoggerFactory.getLogger(AddonManager::class.java)
    private val addons = mutableListOf<Addon>()
    private val pendingAddons = mutableListOf<Addon>()
    fun addons(): List<Addon> {
        return addons
    }


    override fun initialize(context: ConfigurableApplicationContext) {
        logger.info("Initializing AddonManager")
        val dir = File("addons")
        if (!dir.exists()) dir.mkdir()
        val files = dir.listFiles()?.filter { it.isFile && it.extension == "jar" } ?: emptyList()
        val urls = files.map { it.toURI().toURL() }.toTypedArray()
        val classLoader = URLClassLoader(urls, ClassUtils.getDefaultClassLoader())
        ClassUtils.overrideThreadContextClassLoader(classLoader)
        for (file in files) {
            val addon = Addon(file, context)
            lateinit var loader: AddonLoader
            addons.add(addon)

            val depends = addon.config.depends ?: emptyList()
            val softDepends = addon.config.softDepends ?: emptyList()

            val isEmptyDepends = depends.isEmpty() && softDepends.isEmpty()
            if (isEmptyDepends) {
                loader = addon.loader()
                loader.init()
                continue
            }
            if (addons.any { depends.contains(it.config.name) } || addons.any { softDepends.contains(it.config.name) }) {
                loader = addon.loader()
                loader.init()
                continue
            }

            pendingAddons.add(addon)
        }

        while (pendingAddons.isNotEmpty()) {
            val tempAddons = mutableListOf(*pendingAddons.toTypedArray())
            for (addon in pendingAddons) {
                val depends = addon.config.depends ?: emptyList()
                val softDepends = addon.config.softDepends ?: emptyList()

                val isEmptyDepends = depends.isEmpty() && softDepends.isEmpty()
                if (isEmptyDepends) {
                    val loader = addon.loader()
                    loader.init()
                    tempAddons.remove(addon)
                    continue
                }

                if (addons.any { depends.contains(it.config.name) } && depends.isNotEmpty()) {
                    val loader = addon.loader()
                    loader.init()
                    tempAddons.remove(addon)
                    continue
                } else if (!addons.any { depends.contains(it.config.name) } && depends.isNotEmpty()) {
                    tempAddons.remove(addon)
                    logger.error("Addon ${addon.config.name} has unsatisfied dependencies")
                }

                if (addons.any { softDepends.contains(it.config.name) } && softDepends.isNotEmpty()) {
                    val loader = addon.loader()
                    loader.init()
                    tempAddons.remove(addon)
                    continue
                } else if (!addons.any { softDepends.contains(it.config.name) } && softDepends.isNotEmpty()) {
                    val loader = addon.loader()
                    loader.init()
                    tempAddons.remove(addon)
                    logger.warn("Addon ${addon.config.name} has unsatisfied dependencies")
                    addon.config.softDepends?.forEach {
                        logger.warn(" - Soft Dependency: $it")
                    }
                    tempAddons.remove(addon)
                    continue
                }
            }
            pendingAddons.clear()
            pendingAddons.addAll(tempAddons)
        }

        val factory = context.beanFactory

        factory.registerSingleton("AddonManagerV1", this)

        val postProcessor = localContainerEntityFactoryBeanProcessor(factory)
        factory.addBeanPostProcessor(postProcessor)

        logger.info("Initialized AddonManager")
    }

    private fun localContainerEntityFactoryBeanProcessor(factory: ConfigurableListableBeanFactory): BeanPostProcessor {
        return object : BeanPostProcessor {
            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
                if (bean is DataSource) {
                    val packages = addons()
                        .map {
                            it.config.main.split(".").dropLast(1)
                                .joinToString(".")
                        }
                        .toTypedArray()
                    val em = LocalContainerEntityManagerFactoryBean()
                    val corePackage = CoreApplication::class.java.`package`.name
                    em.setPackagesToScan(*packages, corePackage)
                    em.persistenceUnitName = "default"
                    em.beanClassLoader = ClassUtils.getDefaultClassLoader() ?: throw Exception("Default ClassLoader not found")
                    em.persistenceProvider = HibernatePersistenceProvider()
                    em.jpaVendorAdapter = HibernateJpaVendorAdapter()
                    em.dataSource = bean

                    val env = factory.getBean("environment") as Environment

                    val properties = mapOf(
                        "hibernate.hbm2ddl.auto" to env.getProperty("spring.jpa.hibernate.ddl-auto"),
                        "hibernate.dialect" to env.getProperty("spring.jpa.properties.hibernate.dialect"),
                        "hibernate.show_sql" to env.getProperty("spring.jpa.show-sql"),
                        "hibernate.implicit_naming_strategy" to "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
                        "hibernate.physical_naming_strategy" to "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy",
                    )
                    em.setJpaPropertyMap(properties)

                    em.afterPropertiesSet()
                    factory.registerSingleton("entityManagerFactory", em)
                }
                return bean
            }

            override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
                if (bean is CoreApplication) {
                    addons.filter { it.loaded() }.forEach { it.loader().registerAll() }
                    logger.info("Registered (${addons.size}) Addons")
                }
                return bean
            }
        }
    }
}