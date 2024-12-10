package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.ChatRequest
import it.mag.wrongtzap.controller.web.request.FriendRequest
import it.mag.wrongtzap.controller.web.request.NewPasswordRequest
import it.mag.wrongtzap.controller.web.request.UserFetchRequest
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userManager: UserManager,
    private val template: SimpMessagingTemplate,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/user/search")
    fun searchUser(@RequestBody request: UserFetchRequest) = userService.retrieveById(request.userId)

    @PatchMapping("/user/username")
    fun patchUserName(
        @RequestHeader("Authorization") token: String,
        @RequestBody userName: String
    ) = userService.editUserName(jwtUtil.tokenToSubject(token), userName)

    @PatchMapping("/user/password")
    fun patchUserPassword(
        @RequestHeader("Authorization") token: String,
        @RequestBody newPasswordRequest: NewPasswordRequest,
    ) = userManager.changePassword(jwtUtil.tokenToSubject(token), newPasswordRequest)


    @DeleteMapping("/user/delete")
    fun deleteUser(
        @RequestHeader("Authorization") token: String
    ) = userService.deleteUser(jwtUtil.tokenToSubject(token))

    @MessageMapping("/user/friend/add")
    fun addFriend(request: FriendRequest){
        val response = userService.addFriend(request)
        template.convertAndSend("/topic/users/${response.first.userId}", response.first)
        template.convertAndSend("/topic/users/${response.second.userId}", response.second)
    }

    @MessageMapping("/user/friend/remove")
    @SendTo("/topic/users")
    fun removeFriend(request: FriendRequest){
        val response= userService.removeFriend(request)

        template.convertAndSend("/topic/users/${response.first.userId}", response.first)
        template.convertAndSend("/topic/users/${response.second.userId}", response.second)
    }
}