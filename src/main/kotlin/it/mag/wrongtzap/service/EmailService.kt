package it.mag.wrongtzap.service

import it.mag.wrongtzap.util.MailFormUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService @Autowired constructor(
    val emailSender: JavaMailSender,
) {

    fun sendLoginNotification(receiverMail: String, receiverId: String){

        val message = emailSender.createMimeMessage()
        val messageHelper = MimeMessageHelper(message, true, "UTF-8")

        messageHelper.setTo(receiverMail)
        messageHelper.setSubject("Login Activity Detected")
        messageHelper.setText(
            MailFormUtil.loginForm.replace("USERNAME", receiverId), true
        )
        messageHelper.setFrom("wrongtzapp@gmail.com")

        emailSender.send(message)
    }






}

