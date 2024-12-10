package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInChat
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.controller.web.response.JoinDateResponse
import it.mag.wrongtzap.controller.web.response.MessageResponse
import it.mag.wrongtzap.controller.web.response.UserProfile
import it.mag.wrongtzap.jwt.UserDetail
import it.mag.wrongtzap.service.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@DgsComponent
class ChatDataFetcher @Autowired constructor(
    private val chatService: ChatService,
) {
    @DgsQuery(field = "Chat")
    fun getChat(@InputArgument chatId: String) = chatService.retrieveChatById(chatId)

    @DgsQuery(field = "everyChat")
    fun getEveryChat() = chatService.retrieveAllChats()

    @DgsData(parentType = "Chat", field = "participants")
    fun getUsers(dfe: DataFetchingEnvironment):Set<UserProfile>{
        val chat = dfe.getSource<Chat>() ?: throw ChatNotFoundException()

            val responseList: MutableSet<UserProfile> = mutableSetOf()

            chat.participants.forEach{ user ->
                responseList.add(
                    UserProfile(
                    userId = user.userId,
                    username = user.username
            )
                )}

            return responseList
    }

    @DgsData(parentType = "Chat", field = "participantsDate")
    fun getJoinDate(dfe: DataFetchingEnvironment): List<JoinDateResponse>{
        val chat = dfe.getSource<Chat>() ?: throw ChatNotFoundException()
        return chat.userJoinDates.map { JoinDateResponse(userId = it.key, it.value) }
    }

    @DgsData(parentType = "Chat", field = "messages")
    fun getMessages(dfe: DataFetchingEnvironment): List<MessageResponse>{

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as UserDetail
        val email = jwt.getId()

        val chat = dfe.getSource<Chat>() ?: throw ChatNotFoundException()
        val user = chat.participants.firstOrNull{ user -> user.email == email}
            ?: throw UserNotFoundInChat()

        val responseList = chat.messages.filter { message ->
            !message.deletedForEveryone &&
                    Date(message.timestamp ).after(Date(chat.userJoinDates[user.userId] ?: throw UserNotFoundInChat())) &&
                    (message.deletedForUser.isNotEmpty() || !message.deletedForUser.contains(user.userId))
        }
            .map { message ->
                MessageResponse(
                    username = message.sender.username,
                    userId = message.sender.userId,
                    content = message.content,
                    chatId = message.associatedChat.chatId,
                    timestamp = message.timestamp.toFloat()
                )
            }

        return responseList
    }


}