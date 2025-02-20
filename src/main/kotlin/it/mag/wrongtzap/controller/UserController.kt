package it.mag.wrongtzap.controller

import it.mag.wrongtzap.controller.web.page.request.PageChatRequest
import it.mag.wrongtzap.controller.web.user.request.*
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.PaginationManager
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController @Autowired constructor(
    private val userService: UserService,
    private val userManager: UserManager,
    private val paginationManager: PaginationManager,
    private val template: SimpMessagingTemplate,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/user/search")
    fun searchUser(@RequestBody request: ProfileFetchRequest) = userService.retrieveById(request.userId)

    @MessageMapping("/user/pages/chats")
    fun getNextChatPage(request: PageChatRequest){
        val response = paginationManager.nextChatPage(request)
        template.convertAndSend("/topic/${request.userId}/pages/chats", response)
    }

    @MessageMapping("/user/pages/groups")
    fun getNextGroupPage(request: PageChatRequest){
        val response = paginationManager.nextGroupPage(request)
        template.convertAndSend("/topic/${request.userId}/pages/groups", response)
    }

    @MessageMapping("/user/edit/username")
    fun patchUserName(request: EditProfileRequest){
        val response = userService.editUserName(request)
        template.convertAndSend("/topic/users/${response.userId}", response)
    }



    @PatchMapping("/user/password")
    fun patchUserPassword(@RequestBody newPasswordRequest: NewPasswordRequest,
    ) = userManager.changePassword(newPasswordRequest)


    @DeleteMapping("/user/delete")
    fun deleteUser(@RequestBody request: UserDeleteRequest) = userManager.deleteUser(request)

    @MessageMapping("/user/friend/send/request")
    fun sendFriendship(request: PendingFriendRequest){
        val response = userService.sendFriendRequest(request)
        template.convertAndSend("/topic/users/${request.senderId}/friends", response.first)
        template.convertAndSend("/topic/users/${request.receiverId}/friends", response.second)
    }

    @MessageMapping("/user/friend/accept/request")
    fun acceptFriendship(request: UpdateFriendRequest){
        val response = userService.acceptRequest(request)
        template.convertAndSend("/topic/users/${request.senderId}/friends", response.first)
        template.convertAndSend("/topic/users/${request.receiverId}/friends", response.second)
    }

    @MessageMapping("/user/friend/reject/request")
    fun rejectFriendship(request: UpdateFriendRequest){
        val response = userService.rejectRequest(request)
        template.convertAndSend(
            "/topic/users/${request.senderId}/friends",
            "friendshipId" to response
        )
        template.convertAndSend(
            "/topic/users/${request.receiverId}/friends",
            "friendshipId" to response
        )
    }

//    @MessageMapping("/user/friend/remove")
//    fun removeFriend(request: PendingFriendRequest){
//        val response= userService.removeFriend(request)
//
//        template.convertAndSend("/topic/users/${response.first.userId}/friends", response.second)
//        template.convertAndSend("/topic/users/${response.second.userId}/friends", response.first)
//    }
}