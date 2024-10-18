package dev.emirman.mp.addon.notifier.model

class PushNotification {
    lateinit var oneSignal: OneSignal
    lateinit var templates: List<Template>

    class OneSignal {
        lateinit var appId: String
        lateinit var apiKey: String
    }

    class Template {
        lateinit var name: String
        lateinit var title: String
        lateinit var message: String
    }
}