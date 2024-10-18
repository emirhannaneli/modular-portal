package dev.emirman.mp.addon.multiple.locale.service.translator

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import java.util.*

@Service
class TranslatorService {
    fun translate(text: String, target: Locale): String {
        val base = "https://script.google.com/macros/s/AKfycbwmr5iGX_--1QiPowYqAg4m2zmQtISIJLz7L3kf70Y4MPvyGDXnYMA8OKDku0anH5GcyA/exec"
        val params = mapOf("q" to text, "target" to target.language)
            .entries.joinToString("&") { (k, v) -> "$k=$v" }

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$base?$params")
            .build()
        val response = client.newCall(request).execute()
        return response.body?.string()?.let { decodeHtmlEntities(it) } ?: throw RuntimeException("Failed to translate")
    }

    private fun decodeHtmlEntities(input: String): String {
        return input.replace("&#(\\d+);".toRegex()) { matchResult ->
            val charCode = matchResult.groupValues[1].toInt()
            charCode.toChar().toString()
        }
    }
}