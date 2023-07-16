package one.njk.celestidesk.utils

import one.njk.celestidesk.BuildConfig
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun sendEmail(subject: String, body: String) {
    // SMTP server configuration for Gmail
    val properties = Properties()
    properties["mail.smtp.host"] = "smtp.gmail.com"
    properties["mail.smtp.port"] = "587"
    properties["mail.smtp.auth"] = "true"
    properties["mail.smtp.starttls.enable"] = "true"

    // Sender and recipient details
    val senderEmail = BuildConfig.MAIL_ID
    val senderPassword = BuildConfig.MAIL_PWD
    val recipientEmail = BuildConfig.MAIL_TO

    // Create a session with SMTP authentication
    val session = Session.getInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(senderEmail, senderPassword)
        }
    })

    try {
        // Create a new email message
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(senderEmail))
        message.addRecipient(Message.RecipientType.TO, InternetAddress(recipientEmail))
        message.subject = subject
        message.setText(body)

        // Send the email
        Transport.send(message)

        // Email sent successfully
        println("Email sent successfully")
    } catch (e: MessagingException) {
        // Handle exception
        println("Error sending email: ${e.message}")
    }
}
