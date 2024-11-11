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
import it.mag.wrongtzap.controller.web.response.UserResponse
import it.mag.wrongtzap.jwt.UserDetail
import it.mag.wrongtzap.service.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

@DgsComponent
class ChatDataFetcher @Autowired constructor(
    private val chatService: ChatService
) {
    @DgsQuery(field = "Chat")
    fun getChat(@InputArgument chatId: String) = chatService.retrieveChatById(chatId)

    @DgsQuery(field = "everyChat")
    fun getEveryChat() = chatService.retrieveAllChats()

    @DgsData(parentType = "Chat", field = "participants")
    fun getUsers(dfe: DataFetchingEnvironment):Set<UserResponse>{
        val chat = dfe.getSource<Chat>() ?: throw it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException()

            val responseList: MutableSet<UserResponse> = mutableSetOf()

            chat.participants.forEach{ user ->
                responseList.add(
                    UserResponse(
                    userId = user.userId,
                    email = user.email,
                    username = user.username
            )
                )}

            return responseList
    }

    @DgsData(parentType = "Chat", field = "participantsDate")
    fun getJoinDate(dfe: DataFetchingEnvironment): List<JoinDateResponse>{

        val chat = dfe.getSource<Chat>() ?: throw it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException()
        return chat.userJoinDates.map { JoinDateResponse(userId = it.key, it.value) }
    }

    @DgsData(parentType = "Chat", field = "messages")
    fun getMessages(dfe: DataFetchingEnvironment): List<MessageResponse>{

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as UserDetail
        val userId = jwt.getId()

        val chat = dfe.getSource<Chat>() ?: throw ChatNotFoundException()
        val user = chat.participants.firstOrNull{ user -> user.email == userId}
            ?: throw UserNotFoundInChat()

        val responseList = chat.messages.filter { message ->
            !message.deletedForEveryone &&
                        !message.deletedForUser.contains(user.userId) &&
                        message.timestamp.isAfter(
                            chat.userJoinDates[user.userId]
                        )
        }
            .map { message ->
                MessageResponse(
                    sender = message.sender.userId,
                    content = message.content,
                    timestamp = message.timestamp
                )
            }

        return responseList

    }

}