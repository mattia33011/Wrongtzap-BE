package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.request.MessageRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chats")
class ChatController @Autowired constructor(
    val chatService: ChatService,
    val userManager: UserManager,
){

    @PostMapping
    @JsonView(ViewsConfig.Public::class)
    fun postChat(@RequestBody chatRequest: ChatRequest) = userManager.createChat(chatRequest)

    @PostMapping("/{chatId}/users")
    @JsonView(ViewsConfig.Public::class)
    fun postChatUser(@PathVariable chatId: String, @RequestBody user: User) = chatService.addChatUser(chatId, user)

    @PostMapping("/{chatId}/messages")
    @JsonView(ViewsConfig.Public::class)
    fun postChatMessage(

        @PathVariable chatId: String,
        @RequestBody request: MessageRequest

    ) = userManager.createMessage(chatId, request)


    @PatchMapping("/{chatId}")
    @JsonView(ViewsConfig.Public::class)
    fun patchChatName(@PathVariable chatId: String, @RequestBody chatName: String) = chatService.editChatName(chatId,chatName)

    @PatchMapping("/{chatId}/users/{userId}")
    @JsonView(ViewsConfig.Public::class)
    fun patchChatUser(
        @PathVariable chatId: String,
        @PathVariable userId:String
    ) = chatService.removeChatUser(chatId,userId)

    @PatchMapping("/{chatId}/messages/{messageId}")
    @JsonView(ViewsConfig.Public::class)
    fun patchChatMessage(
        @PathVariable chatId: String,
        @PathVariable messageId: String,
        @RequestBody newMessageBody: String
    ) = chatService.editChatMessage(chatId,messageId,newMessageBody)


    @DeleteMapping("/{chatId}")
    @JsonView(ViewsConfig.Public::class)
    fun deleteChat(@PathVariable chatId: String) = chatService.deleteChat(chatId)
}