package dev.emirman.mp.addon.notifier.service.mail

import java.io.File

interface MailService {
    fun send(to: String, templateName: String, data: Map<String, String>, attachments: List<File>, cc: List<String>, bcc: List<String>)
    fun send(to: String, templateName: String, data: Map<String, String>, attachments: List<File>)
    fun send(to: String, templateName: String, data: Map<String, String>)
    fun send(to: String, templateName: String)
}