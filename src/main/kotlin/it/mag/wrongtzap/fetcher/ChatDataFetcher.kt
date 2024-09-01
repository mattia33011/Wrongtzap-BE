package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.response.MessageResponse
import it.mag.wrongtzap.response.UserResponse
import it.mag.wrongtzap.service.ChatService
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class ChatDataFetcher @Autowired constructor(
    private val chatService: ChatService
) {
    @DgsQuery(field = "Chat")
    fun getChat(@InputArgument chatId: String) = chatService.retrieveChat(chatId)

    @DgsQuery(field = "everyChat")
    fun getEveryChat() = chatService.retrieveAllChats()

    @DgsData(parentType = "Chat", field = "chatParticipants")
    fun getUsers(dfe: DataFetchingEnvironment):Set<Any>{
        val chat = dfe.getSource<Chat>() ?: throw NullPointerException("Chat Not Found")


            val responseList: MutableSet<UserResponse> = mutableSetOf()

            chat.chatParticipants.forEach{ user ->
                responseList.add(UserResponse(
                    userId = user.userId,
                    userMail = user.userMail,
                    userName = user.userName
            ))}

            return responseList
    }

    @DgsData(parentType = "Chat", field = "chatMessages")
    fun getMessages(dfe: DataFetchingEnvironment): List<MessageResponse>{
        val chat = dfe.getSource<Chat>() ?: throw  NullPointerException("Chat Not Found")

        val responseList: MutableList<MessageResponse> = mutableListOf()
        chat.chatMessages.forEach{ message ->
            responseList.add(
                MessageResponse(
                    messageSender = message.messageSender.userId,
                    messageBody = message.messageBody
            ))}

        return responseList

    }

}