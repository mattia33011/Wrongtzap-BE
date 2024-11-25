package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.response.ChatResponse
import it.mag.wrongtzap.controller.web.response.JoinDateResponse
import it.mag.wrongtzap.controller.web.response.MessageResponse
import it.mag.wrongtzap.controller.web.response.UserResponse
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
            users = chat.participants.map { user -> userToResponse(user) }.toMutableSet(),
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
            userId =  user.userId,
            username = user.username,
            email = user.email
        )
        return response
    }

    fun messageToResponse(message: Message): MessageResponse{
        val response = MessageResponse(
            content = message.content,
            timestamp = message.timestamp,
            chatId = message.associatedChat.chatId,
            sender = message.sender.userId
        )
        return response
    }
}