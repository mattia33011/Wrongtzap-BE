package it.mag.wrongtzap.manager

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.service.MessageService
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserManager @Autowired constructor(
    val messageService: MessageService,
    val userService: UserService,
    val chatService: ChatService

) {

    fun createChat(chatRequest: ChatRequest): Chat {

        val participants: MutableSet<User> = mutableSetOf()

        chatRequest.chatUsers.forEach{ id ->
            participants.add(
                userService.retrieveById(id).orElseThrow{NullPointerException("User Not Found")}
            )
        }


        val newChat = Chat(
            chatName = chatRequest.chatName,
            chatParticipants = participants,
            chatMessages = mutableListOf()
        )

        return chatService.createChat(newChat)

    }

    fun createMessage(userId:String, chatId: String, body: String){
        val user = userService.retrieveById(userId).orElseThrow { NullPointerException("UserNotFound") }
        val message = Message(messageSender = user, messageBody = body)

        chatService.addChatMessage(chatId,messageService.createMessage(message))
    }


}
