package it.mag.wrongtzap.util

object MailFormUtil {
    val loginForm = "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "  <style>" +
            "    .email-body { font-family: Arial, sans-serif; }" +
            "    .header { background-color: #f2f2f2; padding: 20px; text-align: center; }" +
            "    .content { padding: 20px; }" +
            "    .footer { background-color: #f2f2f2; padding: 10px; text-align: center; font-size: 12px; }" +
            "  </style>" +
            "</head>" +
            "<body class='email-body'>" +
            "  <div class='header'>" +
            "    <h1>We noticed a login to your account USERNAME</h1>" +
            "  </div>" +
            "  <div class='content'>" +
            "    <p>If this was you</p>" +
            "    <p>You can ignore this message. There's no need to take any action</p>" +
            "    <p>Best Regards,<br/>The Wrongtzapp Team</p>" +
            "  </div>" +
            "</body>" +
            "</html>"
}