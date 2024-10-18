package dev.emirman.mp.core.addon.v1

import dev.emirman.mp.core.model.addon.v1.Addon
import dev.emirman.mp.core.model.addon.v1.AddonConfig
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.AnnotationMetadata
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource
import org.springframework.data.repository.config.RepositoryConfigurationDelegate
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.util.ClassUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer
import java.io.File


class AddonLoader(
    private val context: ConfigurableApplicationContext,
    val config: AddonConfig,
    val file: File,
    val main: Class<MPAddon>,
    val configurations: HashSet<Class<*>>,
    val components: HashSet<Class<*>>,
    val services: HashSet<Class<*>>,
    val controllers: HashSet<Class<*>>,
) {
    @Suppress("UNCHECKED_CAST")
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AddonLoader::class.java)
        fun load(context: ConfigurableApplicationContext, addon: Addon): AddonLoader {
            val loader = ClassUtils.getDefaultClassLoader() ?: throw Exception("Default ClassLoader not found")
            val main = loader.loadClass(addon.config.main)
            if (!MPAddon::class.java.isAssignableFrom(main)) throw Exception("Main class must implement MPAddon")

            val builder = ConfigurationBuilder()
                .addClassLoaders(loader)
                .setUrls(ClasspathHelper.forClassLoader(loader))

            val reflections = Reflections(builder)

            val configurations = reflections.getTypesAnnotatedWith(Configuration::class.java).toHashSet()
            val components = reflections.getTypesAnnotatedWith(Component::class.java).toHashSet()
            val services = reflections.getTypesAnnotatedWith(Service::class.java).toHashSet()
            val controllers = HashSet<Class<*>>()

            controllers.addAll(reflections.getTypesAnnotatedWith(Controller::class.java))
            controllers.addAll(reflections.getTypesAnnotatedWith(RestController::class.java))

            val addonLoader = AddonLoader(
                context = context,
                config = addon.config,
                file = addon.file,
                main = main as Class<MPAddon>,
                configurations = configurations,
                components = components,
                services = services,
                controllers = controllers,
            )
            val instance = addonLoader.createInstance<MPAddon>(main)
            addonLoader.factory().registerSingleton(addonLoader.config.name, instance)
            val bean = addonLoader.factory().getBean(addonLoader.config.name) as MPAddon
            bean.onEnable()
            logger.info("${addonLoader.config.name} v${addonLoader.config.version} loaded")
            return addonLoader
        }

        fun <T> loadYaml(file: File, type: Class<T>): T {
            val content = file.readText()

            val dumperOptions = DumperOptions()
            dumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.AUTO
            val representer = Representer(dumperOptions)
            representer.propertyUtils.isSkipMissingProperties = true
            val loaderOptions = LoaderOptions()
            loaderOptions.isAllowDuplicateKeys = false
            val constructor = Constructor(type, loaderOptions)
            constructor.propertyUtils.isSkipMissingProperties = true
            constructor.isAllowDuplicateKeys = false

            return Yaml(constructor, representer).loadAs(content, type)
        }

        fun banner(vararg lines: String) {
            banner(logger, *lines)
        }

        fun banner(logger: Logger, vararg lines: String) {
            val separator = lines.maxOf { it.length } + 2
            logger.info("=".repeat(separator + 4))
            lines.forEach { logger.info("|| $it".padEnd(separator + 2) + "||") }
            logger.info("=".repeat(separator + 4))
        }

        fun unload() {
            TODO("Not yet implemented")
        }
    }

    fun init() {
        registerRepositories()
    }

    fun registerAll() {
        registerConfigurations()
        registerServices()
        registerComponents()
        registerRequestMappings()
    }

    fun factory(): ConfigurableListableBeanFactory {
        return context.beanFactory
    }

    private fun registerRepositories() {
        logger.info("Registering External Repositories...")
        try {
            val classLoader = ClassUtils.getDefaultClassLoader() ?: throw Exception("Default ClassLoader not found")
            val registry = context as BeanDefinitionRegistry

            val builder = ConfigurationBuilder()
                .addClassLoaders(classLoader)
                .setUrls(ClasspathHelper.forClassLoader(classLoader))
            val reflections = Reflections(builder)

            val repositories = reflections.getTypesAnnotatedWith(Repository::class.java)
                .map {
                    BeanDefinitionBuilder.genericBeanDefinition(it).beanDefinition
                }.toHashSet()

            val clazz = ClassUtils.forName(config.main, classLoader)

            val metadata = AnnotationMetadata.introspect(clazz)
            val configurationSource = AnnotationRepositoryConfigurationSource(
                metadata,
                EnableJpaRepositories::class.java,
                context,
                context.environment,
                registry,
                null
            )

            val delegate = RepositoryConfigurationDelegate(configurationSource, context, context.environment)

            val extension = JpaRepositoryConfigExtension()
            for (repository in repositories) {
                delegate.registerRepositoriesIn(registry, extension)
            }
        } catch (e: IllegalStateException) {
            logger.warn("If you are doing database related operations use @EnableJpaRepositories('package') in your Main class.")
        }
        logger.info("Registered External Repositories.")
    }

    private fun registerRequestMappings() {
        val mapping = factory().getBean("requestMappingHandlerMapping") as RequestMappingHandlerMapping
        val defaultMappings = mapping.handlerMethods
        defaultMappings.forEach { (key, _) ->
            mapping.unregisterMapping(key)
        }
        for (controller in controllers) {
            val instance = registerBean(controller)
            val requestMethods = controller.methods
                .filter { it.isAnnotationPresent(RequestMapping::class.java) }
            for (method in requestMethods) {
                val handlerMethod = HandlerMethod(instance, method)
                val mappings = method.getAnnotation(RequestMapping::class.java).value
                val info = RequestMappingInfo
                    .paths(*mappings)
                    .mappingName(controller.canonicalName + "#" + method.name)
                    .methods(*method.getAnnotation(RequestMapping::class.java).method)
                    .params(*method.getAnnotation(RequestMapping::class.java).params)
                    .headers(*method.getAnnotation(RequestMapping::class.java).headers)
                    .consumes(*method.getAnnotation(RequestMapping::class.java).consumes)
                    .produces(*method.getAnnotation(RequestMapping::class.java).produces)
                    .build()
                mapping.registerMapping(info, handlerMethod, method)
            }
        }
        mapping.afterPropertiesSet()
    }

    private fun registerConfigurations() {
        configurations.forEach { registerBean(it) }
    }

    private fun registerComponents() {
        components.forEach { registerBean(it) }
    }

    private fun registerServices() {
        services.forEach { registerBean(it) }
    }

    private fun registerBean(clazz: Class<*>, name: String? = null): Any {
        val beanName = name ?: clazz.canonicalName

        val exists = existsBean(beanName)

        if (exists) return factory().getBean(beanName)

        val instance = createInstance<Any>(clazz)

        factory().registerSingleton(beanName, instance)

        if (clazz.isAnnotationPresent(Service::class.java)) {
            clazz.interfaces.forEach { i ->
                factory().registerSingleton(i.canonicalName, instance)
            }
        }

        return factory().getBean(beanName)
    }

    private fun registerBean(clazz: Class<*>, instance: Any) {
        val beanName = clazz.canonicalName
        val registry = factory() as BeanDefinitionRegistry
        val beanDefinition: BeanDefinition = GenericBeanDefinition().apply {
            beanClassName = clazz.canonicalName
            scope = ConfigurableBeanFactory.SCOPE_SINGLETON
        }
        registry.registerBeanDefinition(beanName, beanDefinition)
        factory().registerSingleton(beanName, instance)
        clazz.interfaces.forEach { i ->
            factory().registerSingleton(i.canonicalName, instance)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> createInstance(clazz: Class<*>): T {
        if (clazz.isInterface) throw Exception("Cannot create instance of interface")

        val constructor = clazz.constructors.isEmpty().let {
            if (it) clazz.getDeclaredConstructor()
            else clazz.constructors[0]
        }
        val params = constructor.parameterTypes

        val requiredBeans = params.map { bean ->
            val requiredName = bean.canonicalName
            val requiredExists = existsBean(requiredName)
            if (requiredExists) factory().getBean(requiredName)
            else if (bean.isInterface) {
                factory().getBean(bean)
            } else registerBean(bean)
        }.toTypedArray()
        return constructor.newInstance(*requiredBeans) as T
    }

    private fun existsBean(name: String): Boolean {
        return try {
            factory().getBean(name)
            true
        } catch (e: Exception) {
            false
        }
    }
}
