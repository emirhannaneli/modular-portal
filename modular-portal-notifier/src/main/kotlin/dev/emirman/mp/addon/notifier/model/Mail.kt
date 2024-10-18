package dev.emirman.mp.addon.notifier.model

class Mail {
    lateinit var smtp: SMTP
    lateinit var from: String
    lateinit var templates: List<Template>

    class SMTP {
        lateinit var host: String
        var port: Int = 25
        lateinit var username: String
        lateinit var password: String
    }

    class Template {
        lateinit var name: String
        lateinit var subject: String
        lateinit var htmlFile: String
    }
}