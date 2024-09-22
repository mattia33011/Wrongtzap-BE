package it.mag.wrongtzap.fetcher


import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.exception.user.UserNotFoundInChat
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.response.JoinDateResponse
import it.mag.wrongtzap.response.MessageResponse
import it.mag.wrongtzap.response.UserResponse
import it.mag.wrongtzap.service.ChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import java.time.LocalDateTime

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
        val chat = dfe.getSource<Chat>() ?: throw ChatNotFoundException()

            val responseList: MutableSet<UserResponse> = mutableSetOf()

            chat.participants.forEach{ user ->
                responseList.add(UserResponse(
                    userId = user.userId,
                    email = user.email,
                    username = user.username
            ))}

            return responseList
    }

    @DgsData(parentType = "Chat", field = "participantsDate")
    fun getJoinDate(dfe: DataFetchingEnvironment): List<JoinDateResponse>{

        val chat = dfe.getSource<Chat>() ?: throw  ChatNotFoundException()
        return chat.userJoinDates.map { JoinDateResponse(userId = it.key.userId, it.value) }
    }

    @DgsData(parentType = "Chat", field = "messages")
    fun getMessages(dfe: DataFetchingEnvironment): List<MessageResponse>{

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as Jwt
        val userEmail = jwt.subject

        val chat = dfe.getSource<Chat>() ?: throw  ChatNotFoundException()
        val user = chat.participants.firstOrNull{ user -> user.email == userEmail }
            ?: throw UserNotFoundInChat()

        val responseList = chat.messages.filter { message ->
            !message.deletedForEveryone &&
                        !message.deletedForUser.contains(user.userId) &&
                        message.timestamp.isAfter(chat.userJoinDates.get(user)
                        )
        }
            .map { message ->
                MessageResponse(
                    messageSender = message.sender.userId,
                    messageBody = message.content,
                    timestamp = message.timestamp
                )
            }

        return responseList

    }

}