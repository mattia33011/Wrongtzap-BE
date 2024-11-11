package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.controller.web.request.ChatRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.MessageRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chats")
class ChatController @Autowired constructor(
    private val chatService: ChatService,
    private val userManager: UserManager,
    private val jwtUtil: JwtUtil,
){

    @PostMapping
    @JsonView(ViewsConfig.Public::class)
    fun postChat(@RequestBody chatRequest: ChatRequest) = userManager.createChat(chatRequest)

    @PatchMapping("/{chatId}/users/{userId}")
    @JsonView(ViewsConfig.Public::class)
    fun postChatUser(

        @PathVariable chatId: String,
        @PathVariable userId: String,

    ) = userManager.addUserToChat(chatId, userId)

    @PostMapping("/{chatId}/messages")
    @JsonView(ViewsConfig.Public::class)
    fun postChatMessage(

        @PathVariable chatId: String,
        @RequestBody request: MessageRequest

    ) = userManager.createMessage(chatId, request)


    @PatchMapping("/{chatId}")
    @JsonView(ViewsConfig.Public::class)
    fun patchChatName(

        @PathVariable chatId: String,
        @RequestBody chatName: String

    ) = chatService.editChatName(chatId,chatName)

    @PatchMapping("/{chatId}/users/leave")
    @JsonView(ViewsConfig.Public::class)
    fun patchChatUser(

        @PathVariable chatId: String,
        @RequestHeader("Authorization") token: String

    ) = chatService.leaveGroup(chatId,jwtUtil.tokenToSubject(token))

    @PatchMapping("/{chatId}/messages/{messageId}/self")
    fun deleteMessageForSelf(
        @PathVariable chatId: String,
        @PathVariable messageId: String
    ) = userManager.deleteMessageForSelf(chatId, messageId)

    @PatchMapping("/{chatId}/messages/{messageId}/everyone")
    fun deleteMessageForEveryone(
        @PathVariable chatId: String,
        @PathVariable messageId: String
    ) = userManager.deleteMessageForEveryone(chatId,messageId)


    @DeleteMapping("/{chatId}/users/{userId}")
    @JsonView(ViewsConfig.Internal::class)
    fun removeChatUser(

        @PathVariable chatId: String,
        @PathVariable userId: String,

    ) = chatService.removeUser(chatId, userId)

    @PatchMapping("/{chatId}/messages/{messageId}")
    @JsonView(ViewsConfig.Public::class)
    fun patchChatMessage(

        @PathVariable chatId: String,
        @PathVariable messageId: String,
        @RequestBody newMessageBody: String

    ) = chatService.editMessage(chatId,messageId,newMessageBody)

    @DeleteMapping("/{chatId}/")
    @JsonView(ViewsConfig.Public::class)
    fun deleteChat(@PathVariable chatId: String) = chatService.deleteChat(chatId)
}