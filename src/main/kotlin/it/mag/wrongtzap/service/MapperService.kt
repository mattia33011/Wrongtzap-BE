package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.response.chat.DirectChatResponse
import it.mag.wrongtzap.controller.web.response.chat.GroupChatResponse
import it.mag.wrongtzap.controller.web.response.chat.JoinDateResponse
import it.mag.wrongtzap.controller.web.response.message.MessageResponse
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse
import it.mag.wrongtzap.controller.web.response.user.UserResponse
import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import org.springframework.stereotype.Service

@Service
class MapperService {

    fun groupChatToResponse(chat: GroupChat): GroupChatResponse {
        val response = GroupChatResponse(
            name = chat.name,
            chatId = chat.chatId,
            creationDate = chat.creationDate,
            messages = chat.messages.map { message -> messageToResponse(message) }.toMutableList(),
            users = chat.participants.map { user -> userToProfile(user) }.toMutableSet(),
            joinDate = chat.userJoinDates.map {
                date -> JoinDateResponse(
                    userId = date.key,
                    timestamp = date.value
                )
            }.toMutableSet(),
            archived = chat.archived
        )
        return response
    }


    fun directChatToResponse(chat: DirectChat): DirectChatResponse {
        val response = DirectChatResponse(
            chatId = chat.chatId,
            creationDate = chat.creationDate,
            messages = chat.messages.map { message -> messageToResponse(message) }.toMutableList(),
            participants = chat.participants.map { user -> userToProfile(user) },
            archived = chat.archived
        )
        return response
    }

    fun userToResponse(user: User): UserResponse {
        val response = UserResponse(
            userId = user.userId,
            username = user.username,
            directChats = user.directChats.map { chat -> directChatToResponse(chat) }.toMutableSet(),
            groupChats = user.groupChats.map { chat -> groupChatToResponse(chat) }.toMutableSet(),
            friends = user.friends.map { friend -> userToProfile(friend) }.toMutableSet()
        )
        return response
    }

    fun userToProfile(user: User): ProfileResponse {
        val response = ProfileResponse(
            userId =  user.userId,
            username = user.username,
        )
        return response
    }

    fun messageToResponse(message: Message): MessageResponse {
        val response = MessageResponse(
            content = message.content,
            timestamp = message.timestamp,
            chatId = message.associatedChat.chatId,
            username = message.sender.username,
            userId = message.sender.userId
        )
        return response
    }
}