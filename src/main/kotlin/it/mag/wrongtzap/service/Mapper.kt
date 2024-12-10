package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.response.*
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import org.springframework.stereotype.Service

@Service
class Mapper {

    fun chatToResponse(chat: Chat): ChatResponse{
        val response = ChatResponse(
            name = chat.name,
            chatId = chat.chatId,
            isGroup = chat.isGroup,
            messages = chat.messages.map { message -> messageToResponse(message) }.toMutableList(),
            users = chat.participants.map { user -> userToProfile(user) }.toMutableSet(),
            joinDate = chat.userJoinDates.map {
                date -> JoinDateResponse(
                    userId = date.key,
                    timestamp = date.value
                )
            }.toMutableSet(),
        )
        return response
    }

    fun userToResponse(user: User): UserResponse{
        val response = UserResponse(
            userId = user.userId,
            username = user.username,
            chats = user.chats.map { chat -> chatToResponse(chat) }.toMutableSet(),
            friends = user.friends.map { friend -> userToProfile(friend) }.toMutableSet()
        )

        return response
    }

    fun userToProfile(user: User): UserProfile{
        val response = UserProfile(
            userId =  user.userId,
            username = user.username,
        )
        return response
    }

    fun messageToResponse(message: Message): MessageResponse{
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