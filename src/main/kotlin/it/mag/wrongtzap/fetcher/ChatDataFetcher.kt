package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInChat
import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.controller.web.response.chat.JoinDateResponse
import it.mag.wrongtzap.controller.web.response.message.MessageResponse
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse
import it.mag.wrongtzap.jwt.UserDetail
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.service.DirectChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@DgsComponent
class ChatDataFetcher @Autowired constructor(
    private val chatService: DirectChatService,
) {
    @DgsQuery(field = "Chat")
    fun getChat(@InputArgument chatId: String) = chatService.retrieveChatById(chatId)

    @DgsQuery(field = "everyChat")
    fun getEveryChat() = chatService.retrieveAllChats()

    @DgsData(parentType = "DirectChat", field = "participants")
    fun getChatUsers(dfe: DataFetchingEnvironment):List<ProfileResponse>{
        val chat = dfe.getSource<DirectChat>() ?: throw ChatNotFoundException()

        val responseList: List<ProfileResponse> =
            chat.participants.map{ user ->
                ProfileResponse(
                    userId = user.userId,
                    username = user.username
                )
            }.toList()

        return responseList
    }

    @DgsData(parentType = "GroupChat", field = "participants")
    fun getGroupUsers(dfe: DataFetchingEnvironment):Set<ProfileResponse>{
        val chat = dfe.getSource<GroupChat>() ?: throw ChatNotFoundException()

            val responseList: MutableSet<ProfileResponse> = mutableSetOf()

            chat.participants.forEach{ user ->
                responseList.add(
                    ProfileResponse(
                    userId = user.userId,
                    username = user.username
            )
                )}

            return responseList
    }

    @DgsData(parentType = "GroupChat", field = "participantsDate")
    fun getJoinDate(dfe: DataFetchingEnvironment): List<JoinDateResponse>{
        val chat = dfe.getSource<GroupChat>() ?: throw ChatNotFoundException()
        return chat.userJoinDates.map { JoinDateResponse(userId = it.key, it.value) }
    }

    @DgsData(parentType = "DirectChat", field = "messages")
    fun getChatMessages(dfe: DataFetchingEnvironment): List<MessageResponse>{

        val chat = dfe.getSource<DirectChat>() ?: throw ChatNotFoundException()

        val responseList = chat.messages.filter {
            message -> !message.deletedForEveryone
        }
            .map { message -> MessageResponse(
                username = message.sender.username,
                userId = message.sender.userId,
                content = message.content,
                chatId = message.associatedChat.chatId,
                timestamp = message.timestamp.toFloat()
            )}

        return responseList
    }

    @DgsData(parentType = "GroupChat", field = "messages")
    fun getGroupMessages(dfe: DataFetchingEnvironment): List<MessageResponse>{

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as UserDetail
        val email = jwt.getId()



        val chat = dfe.getSource<GroupChat>() ?: throw ChatNotFoundException()
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

    @DgsData(parentType = "DirectChat", field = "archived")
    fun getChatArchivedIds(dfe: DataFetchingEnvironment): List<String>{
        val chat = dfe.getSource<DirectChat>()
            ?: throw ChatNotFoundException()

        return chat.archived
    }


    @DgsData(parentType = "GroupChat", field = "archived")
    fun getGroupArchivedIds(dfe: DataFetchingEnvironment): MutableList<String>{
        val chat = dfe.getSource<GroupChat>()
            ?: throw ChatNotFoundException()

        return chat.archived
    }


}