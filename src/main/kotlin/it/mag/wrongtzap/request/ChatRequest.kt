package it.mag.wrongtzap.request

data class ChatRequest(
   val chatName: String,
   val chatUsers: List<String>
) {}