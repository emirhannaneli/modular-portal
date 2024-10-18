package dev.emirman.mp.addon.notifier.service.mail

import dev.emirman.mp.addon.notifier.config.NotifierConfig
import net.lubble.util.exception.NotFoundException
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import java.io.File

@Service
class IMailService : MailService {
    override fun send(
        to: String,
        templateName: String,
        data: Map<String, String>,
        attachments: List<File>,
        cc: List<String>,
        bcc: List<String>
    ) {
        val settings = NotifierConfig.settings()
        val mailConfig = settings.mail

        val template = mailConfig.templates
            .find { it.name == templateName } ?: throw NotFoundException("template", templateName)

        val notifier = File("addons", "notifier")
        val templates = File(notifier, "templates")
        val htmlContent = File(templates, template.htmlFile).readText()

        data.forEach { (key, value) ->
            htmlContent.replace("{{$key}}", value)
        }

        val smtp = mailConfig.smtp

        val emailBuilder = EmailBuilder.startingBlank()
            .to(to)
            .from(mailConfig.from)
            .withSubject(template.subject)
            .withHTMLText(htmlContent)

        attachments.forEach { file ->
            emailBuilder.withAttachment(file.name, file.readBytes(), file.extension)
        }

        cc.forEach { emailBuilder.cc(it) }
        bcc.forEach { emailBuilder.bcc(it) }

        val email = emailBuilder.buildEmail()

        val mailer = MailerBuilder
            .withSMTPServerHost(smtp.host)
            .withSMTPServerPort(smtp.port)
            .withSMTPServerUsername(smtp.username)
            .withSMTPServerPassword(smtp.password)
            .async()
            .buildMailer()

        mailer.sendMail(email)
    }

    override fun send(to: String, templateName: String, data: Map<String, String>, attachments: List<File>) {
        send(to, templateName, data, attachments, emptyList(), emptyList())
    }

    override fun send(to: String, templateName: String, data: Map<String, String>) {
        send(to, templateName, data, emptyList())
    }

    override fun send(to: String, templateName: String) {
        send(to, templateName, emptyMap())
    }

    @EventListener(ApplicationReadyEvent::class)
    fun test() {
        send("emirhannaneli@gmail.com", "test")
    }
}