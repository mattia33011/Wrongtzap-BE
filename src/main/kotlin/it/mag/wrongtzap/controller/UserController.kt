package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.request.UserRequest
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

    @PostMapping("/{userId}/chats")
    @JsonView(ViewsConfig.Public::class)
    fun postUserChat(
        @PathVariable userId: String,
        @RequestBody chatRequest: ChatRequest
    ) = userManager.createChat(chatRequest)

    @PatchMapping("/{userId}/username")
    @JsonView(ViewsConfig.Public::class)
    fun patchUserName(@PathVariable userId: String, @RequestBody userName: String) = userService.editUserName(userId, userName)

    @PatchMapping("/{userId}/email")
    @JsonView(ViewsConfig.Public::class)
    fun patchEmail(@PathVariable userId: String, @RequestBody userMail: String) = userService.editUserMail(userId, userMail)

    @PatchMapping("/{userId}/chats/{chatId}")
    @JsonView(ViewsConfig.Public::class)
    fun leaveChat(@PathVariable userId: String, @PathVariable chatId: String) = userService.leaveChat(userId,chatId)

    @DeleteMapping("/{userId}")
    @JsonView(ViewsConfig.Public::class)
    fun deleteUser(@PathVariable userId: String) = userService.deleteUser(userId)

}