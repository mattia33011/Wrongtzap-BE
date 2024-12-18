package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.ChatRequest
import it.mag.wrongtzap.controller.web.request.NewPasswordRequest
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userManager: UserManager,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/chats")
    @JsonView(ViewsConfig.Public::class)
    fun postUserChat(
        @RequestBody chatRequest: ChatRequest
    ) = userManager.createChat(chatRequest)

    @PatchMapping("/username")
    @JsonView(ViewsConfig.Public::class)
    fun patchUserName(
        @RequestHeader("Authorization") token: String,
        @RequestBody userName: String
    ) = userService.editUserName(jwtUtil.tokenToSubject(token), userName)

    @PatchMapping("/password")
    fun patchUserPassword(
        @RequestHeader("Authorization") token: String,
        @RequestBody newPasswordRequest: NewPasswordRequest,
    ) = userManager.changePassword(jwtUtil.tokenToSubject(token), newPasswordRequest)


    @DeleteMapping("/delete")
    @JsonView(ViewsConfig.Public::class)
    fun deleteUser(
        @RequestHeader("Authorization") token: String
    ) = userService.deleteUser(jwtUtil.tokenToSubject(token))

}