package it.mag.wrongtzap.request

data class ChatRequest(
   val chatName: String,
   val chatUsersIds: List<String>,
   val isGroup: Boolean
)