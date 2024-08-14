package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.service.UserService
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
@RequestMapping("/users")

class UserController @Autowired constructor(
    private val userService: UserService,
    private val userManager: UserManager
) {

    @GetMapping()
    @JsonView(ViewsConfig.Public::class)
    fun getAllUsers() = userService.retrieveAllUsers()

    @GetMapping("/{userId}")
    @JsonView(ViewsConfig.Public::class)
    fun getUser(@PathVariable userId:String) = userService.retrieveById(userId)

    @GetMapping("/{userId}/chats")
    @JsonView(ViewsConfig.Public::class)
    fun getAllUserChats(@PathVariable userId: String) = userService.retrieveAllChats(userId)

    @GetMapping("/{userId}/chats/{chatId}")
    @JsonView(ViewsConfig.Public::class)
    fun getUserChat(
        @PathVariable userId: String,
        @PathVariable chatId: String)
    = userService.retrieveChat(userId,chatId)

    @GetMapping("/{userId}/messages/{messageBody}")
    @JsonView(ViewsConfig.Public::class)
    fun searcMessages(
        @PathVariable userId: String,
        @PathVariable messageBody: String
    ) = userService.searchMessages(userId, messageBody)

    @PostMapping
    fun postUser(@RequestBody user: User) = userService.createUser(user)

    @PostMapping("/{userId}/chats")
    fun postUserChat(
        @PathVariable userId: String,
        @RequestBody chatRequest: ChatRequest
    ) = userManager.createChat(chatRequest)

    @PostMapping("{userId}/chats/{chatId}/messages")
    fun postUserMessage(@PathVariable userId: String,
                        @PathVariable chatId: String,
                        @RequestBody body: String)
    = userManager.createMessage(userId, chatId, body)


    @PatchMapping("/{userId}/username")
    fun patchUserName(@PathVariable userId: String, @RequestBody userName: String) = userService.editUserName(userId, userName)
    @PatchMapping("/{userId}/email")
    fun patchEmail(@PathVariable userId: String, @RequestBody userMail: String) = userService.editUserMail(userId, userMail)
    @PatchMapping("/{userId}/chats/{chatId}")
    fun leaveChat(@PathVariable userId: String, @PathVariable chatId: String) = userService.leaveChat(userId,chatId)

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String) = userService.deleteUser(userId)

}