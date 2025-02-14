package it.mag.wrongtzap.controller.web.request.chat

data class DirectChatRequest(
   val firstUserId: Long,
   val secondUserId: Long
)