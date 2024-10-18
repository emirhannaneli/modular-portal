package dev.emirman.mp.addon.multiple.locale.controller.locales

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("v1/ml/locales")
class LocaleController {
    @GetMapping(produces = ["application/json"])
    fun getLocales(locale: Locale): List<Map<String, String>> {
        val locales = Locale.getAvailableLocales()
        val list = mutableListOf<Map<String, String>>()
        for (available in locales) {
            if (available.language == "" || available.country == "") continue
            list.add(
                mapOf(
                    "value" to available.toLanguageTag(),
                    "language" to available.getDisplayLanguage(locale),
                    "country" to available.getDisplayCountry(locale),
                )
            )
        }
        return list
    }
}