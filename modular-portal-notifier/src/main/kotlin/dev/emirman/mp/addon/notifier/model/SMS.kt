package dev.emirman.mp.addon.notifier.model

class SMS {
    lateinit var twilio: Twilio
    lateinit var templates: List<Template>

    class Twilio {
        lateinit var accountSid: String
        lateinit var authToken: String
        lateinit var from: String
    }

    class Template {
        lateinit var name: String
        lateinit var message: String
    }
}