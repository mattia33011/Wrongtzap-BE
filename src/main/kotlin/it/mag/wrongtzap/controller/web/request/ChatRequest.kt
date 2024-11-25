package it.mag.wrongtzap.controller.web.request

data class ChatRequest(
   val chatName: String,
   val chatUsersIds: List<String>,
   val isGroup: Boolean
)