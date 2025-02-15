package it.mag.wrongtzap.controller.web.request.chat

data class DirectChatRequest(
   val firstUserId: String,
   val secondUserId: String
)