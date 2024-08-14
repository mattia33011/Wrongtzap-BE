package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.manager.UserManager
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
    //
    // PostMappings
    //

    @PostMapping
    fun postChat(@RequestBody chatRequest: ChatRequest) = userManager.createChat(chatRequest)
    @PostMapping("/{chatId}/users")
    fun postChatUser(@PathVariable chatId: String, @RequestBody user: User) = chatService.addChatUser(chatId, user)

    //
    // Chat GetMappings
    //

    @GetMapping()
    @JsonView(ViewsConfig.Public::class)
    fun getAllChats() = chatService.retrieveAllChats()
    @GetMapping("/{chatId}")
    @JsonView(ViewsConfig.Public::class)
    fun getChat(@PathVariable chatId: String) = chatService.retrieveChat(chatId)

    //
    // User GetMappings
    //

    @GetMapping("/{chatId}/users")
    @JsonView(ViewsConfig.Public::class)
    fun getAllChatUsers(@PathVariable chatId: String) = chatService.retrieveAllChatUsers(chatId)

    @GetMapping("/{chatId}/users/{userId}")
    @JsonView(ViewsConfig.Public::class)

    fun getChatUser(@PathVariable chatId: String, @PathVariable userId: String) = chatService.retrieverChatUser(chatId,userId)

    //
    // Chat GetMappings
    //

    @GetMapping("{chatId}/messages")
    @JsonView(ViewsConfig.Public::class)
    fun getChatMessages(@PathVariable chatId: String) = chatService.retrieveAllChatMessages(chatId)

    @GetMapping("{chatId}/messages/{messageBody}")
    @JsonView(ViewsConfig.Public::class)
    fun searchChatMessages(
        @PathVariable chatId: String,
        @PathVariable messageBody: String
    ) = chatService.searchChatMessages(chatId,messageBody)

    // Patch Mappings

    @PatchMapping("/{chatId}")
    fun patchChatName(@PathVariable chatId: String, @RequestBody chatName: String) = chatService.editChatName(chatId,chatName)

    @PatchMapping("/{chatId}/users/{userId}")
    fun patchChatUser(
        @PathVariable chatId: String,
        @PathVariable userId:String
    ) = chatService.removeChatUser(chatId,userId)

    @PatchMapping("/{chatId}/messages/{messageId}")
    fun patchChatMessage(
        @PathVariable chatId: String,
        @PathVariable messageId: Long,
        @RequestBody newMessageBody: String
    ) = chatService.editChatMessage(chatId,messageId,newMessageBody)


    @DeleteMapping("/{chatId}")
    fun deleteChat(@PathVariable chatId: String) = chatService.deleteChat(chatId)
}