package it.mag.wrongtzap.controller

import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.controller.web.request.ChatRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.MessageRequest
import it.mag.wrongtzap.model.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.messaging.simp.SimpMessagingTemplate

@Controller
@MessageMapping("/chat")
class ChatController @Autowired constructor(
    private val chatService: ChatService,
    private val userManager: UserManager,
    private val template: SimpMessagingTemplate,
    private val jwtUtil: JwtUtil,
){

    //
    // ENTITY CREATION
    //

    @MessageMapping("/create")
    @SendTo("/topic/chats")
    fun createChat(chatRequest: ChatRequest) = userManager.createChat(chatRequest)

    @MessageMapping("/{chatId}/users/{userId}/add")
    fun addUser(@DestinationVariable chatId: String, @DestinationVariable userId: String){
        val user = userManager.addUserToChat(chatId, userId)
        template.convertAndSend("topic/chats/users", user)
    }


    @MessageMapping("/message/add")
    fun postMessage(request: MessageRequest){
        val message = userManager.createMessage(request)
        template.convertAndSend("/topic/chats/messages", message)
    }

    //
    // PROPERTY EDITING
    //

    @MessageMapping("/{chatId}/name")
    fun editChat(@DestinationVariable chatId: String, chatName: String){
        val chat = chatService.editChatName(chatId,chatName)
        template.convertAndSend("topic/chats", chat)
    }

    @MessageMapping("/{chatId}/messages/{messageId}/edit")
    fun editMessage(

        @DestinationVariable chatId: String,
        @DestinationVariable messageId: String,
        newMessageBody: String

    ){
        val message = chatService.editMessage(chatId,messageId,newMessageBody)
        template.convertAndSend("/topic/chats/messages", message)
    }

    //
    //USER REMOVAL
    //

    //WIP
    @MessageMapping("/{chatId}/users/leave")
    fun leaveChat(@DestinationVariable chatId: String, @RequestHeader("Authorization") token: String) {
        chatService.leaveGroup(chatId,jwtUtil.tokenToSubject(token))
        template.convertAndSend("topic/chats/$chatId/users")
    }

    //WIP
    @MessageMapping("/{chatId}/users/{userId}/remove")
    fun removeUser(@DestinationVariable chatId: String, @DestinationVariable userId: String){
        chatService.removeUser(chatId, userId)
        template.convertAndSend("/topic/chats/$chatId/users")
    }

    //
    // MESSAGE DELETION
    //

    //WIP
    @MessageMapping("/{chatId}/messages/{messageId}/delete/self")
    fun deleteMessageForSelf(@DestinationVariable chatId: String, @DestinationVariable messageId: String){
        userManager.deleteMessageForSelf(chatId, messageId)
        template.convertAndSend("/topic/chats/messages")
    }

    //WIP
    @MessageMapping("/{chatId}/messages/{messageId}/delete/everyone")
    fun deleteMessageForEveryone(@DestinationVariable chatId: String, @DestinationVariable messageId: String){
        userManager.deleteMessageForEveryone(chatId,messageId)
        template.convertAndSend("/topic/chats/messages")
    }

    //
    // CHAT DELETION
    //

    //WIP
    @MessageMapping("/{chatId}/delete")
    @SendTo("/topic/chats")
    fun deleteChat(@PathVariable chatId: String) = chatService.deleteChat(chatId)
}