package it.mag.wrongtzap.controller

import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.user.FriendRequest
import it.mag.wrongtzap.controller.web.request.user.NewPasswordRequest
import it.mag.wrongtzap.controller.web.request.user.ProfileFetchRequest
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userManager: UserManager,
    private val template: SimpMessagingTemplate,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/user/search")
    fun searchUser(@RequestBody request: ProfileFetchRequest) = userService.retrieveById(request.userId)

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
        template.convertAndSend("/topic/users/${response.first.userId}/friends", response.second)
        template.convertAndSend("/topic/users/${response.second.userId}/friends", response.first)
    }

    @MessageMapping("/user/friend/remove")
    fun removeFriend(request: FriendRequest){
        val response= userService.removeFriend(request)

        template.convertAndSend("/topic/users/${response.first.userId}/friends", response.second)
        template.convertAndSend("/topic/users/${response.second.userId}/friends", response.first)
    }
}