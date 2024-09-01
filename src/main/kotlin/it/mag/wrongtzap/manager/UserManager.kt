package it.mag.wrongtzap.manager

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.request.MessageRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.service.MessageService
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

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
                userService.retrieveById(id)
            )
        }


        val newChat = Chat(
            chatName = chatRequest.chatName,
            chatParticipants = participants,
            chatMessages = mutableListOf()
        )

        return chatService.createChat(newChat)

    }

    fun createMessage(chatId: String, request: MessageRequest): Chat{

        val user = userService.retrieveById(request.userId)
        val chat = chatService.retrieveChat(chatId)

        val message = Message(messageSender = user, messageBody = request.messageBody, messageChat = chat )
        return chatService.addChatMessage(chatId,messageService.createMessage(message))
    }


}
