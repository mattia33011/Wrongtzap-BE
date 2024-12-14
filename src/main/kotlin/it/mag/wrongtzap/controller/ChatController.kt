package it.mag.wrongtzap.controller

import it.mag.wrongtzap.controller.web.request.chat.DirectChatRequest
import it.mag.wrongtzap.controller.web.request.chat.GroupChatRequest
import it.mag.wrongtzap.controller.web.request.message.MessageDeletionRequest
import it.mag.wrongtzap.controller.web.request.message.MessageRequest
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.ChatManager
import it.mag.wrongtzap.model.type.ChatRequestType
import it.mag.wrongtzap.model.type.ChatResponseType
import it.mag.wrongtzap.service.DirectChatService
import it.mag.wrongtzap.service.GroupChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

@Controller
class ChatController @Autowired constructor(
    private val groupChatService: GroupChatService,
    private val chatManager: ChatManager,
    private val template: SimpMessagingTemplate,
    private val jwtUtil: JwtUtil,
    ){

        //
        // ENTITY CREATION
        //

        @MessageMapping("/chats/create")
        @SendTo("/topic/chats")
        fun createChat(chatRequest: DirectChatRequest) = chatManager.createChat(ChatRequestType.Direct(chatRequest))

        @MessageMapping("/groups/create")
        @SendTo("/topic/groups")
        fun createGroup(chatRequest: GroupChatRequest) = chatManager.createChat(ChatRequestType.Group(chatRequest))


        @MessageMapping("/groups/{groupId}/users/{userId}/add")
        fun addUserToGroup(@DestinationVariable groupId: String, @DestinationVariable userId: String){
            val user = chatManager.addUserToGroup(groupId, userId)
            template.convertAndSend("/topic/groups/users", user)
        }

        @MessageMapping("/messages/add")
        fun postMessage(request: MessageRequest){
        val message = chatManager.createMessage(request, request.type)
            if(request.type == "group")
                template.convertAndSend("/topic/groups/messages", message)
            else
                template.convertAndSend("/topic/chats/messages", message)
    }

        //
        // PROPERTY EDITING
        //

        @MessageMapping("/groups/{groupId}/name")
        fun editGroupName(@DestinationVariable groupId: String, chatName: String){
            val chat = groupChatService.editChatName(groupId,chatName)
            template.convertAndSend("/topic/groups", chat)
        }

//        @MessageMapping("/{chatId}/messages/{messageId}/edit")
//        fun editMessage(
//
//            @DestinationVariable chatId: String,
//            @DestinationVariable messageId: String,
//            newMessageBody: String
//        ){
//            val message = chatService.editMessage(chatId,messageId,newMessageBody)
//            template.convertAndSend("/topic/chats/messages", message)
//        }

        //
        //USER REMOVAL
        //

        //WIP
        @MessageMapping("/groups/{groupId}/users/leave")
        fun leaveGroup(@DestinationVariable groupId: String, @RequestHeader("Authorization") token: String) {
            groupChatService.leaveGroup(groupId,jwtUtil.tokenToSubject(token))
            template.convertAndSend("topic/groups/$groupId/users")
        }

        //WIP
        @MessageMapping("/{chatId}/users/{userId}/remove")
        fun removeUser(@DestinationVariable chatId: String, @DestinationVariable userId: String){
            groupChatService.removeUser(chatId, userId)
            template.convertAndSend("/topic/groups/$chatId/users")
        }

        //
        // MESSAGE DELETION
        //

        //WIP
        @MessageMapping("/messages/delete/self")
        fun deleteMessageForSelf(deleteRequest: MessageDeletionRequest){
            chatManager.deleteMessageForSelf(deleteRequest)
            if(deleteRequest.type == "group")
                template.convertAndSend("/topic/groups/messages")
            else
                template.convertAndSend("/topic/chats/messages")
        }

        //WIP
        @MessageMapping("/messages/delete/everyone")
        fun deleteMessageForEveryone(deleteRequest: MessageDeletionRequest){
            chatManager.deleteMessageForEveryone(deleteRequest)
            if(deleteRequest.type == "group")
                template.convertAndSend("/topic/groups/messages")
            else
                template.convertAndSend("/topic/chats/messages")
        }

        //
        // CHAT DELETION
        //
    }