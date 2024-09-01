package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.response.ChatResponse
import it.mag.wrongtzap.response.MessageResponse
import it.mag.wrongtzap.service.UserService

import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class UserDataFetcher @Autowired constructor(
    private val userService: UserService
) {

    @DgsQuery(field = "user")
    fun getUser(@InputArgument userId: String) = userService.retrieveById(userId)


    @DgsQuery(field = "everyUser")
    fun getAllUsers() = userService.retrieveAllUsers()


    @DgsData(parentType = "User", field = "userChats")
    fun getChats(dfe: DgsDataFetchingEnvironment): Set<ChatResponse> {

        val user = dfe.getSource<User>() ?: throw NullPointerException("User Not Found")

            val responseList: MutableSet<ChatResponse> = mutableSetOf()

            user.userChats.forEach { chat ->
                responseList.add(
                    ChatResponse(
                        chatName = chat.chatName,
                        chatId = chat.chatId
                    )
                )
            }

            return responseList
    }

    @DgsData(parentType = "User", field = "userMessages")
    fun getMessages(dfe: DataFetchingEnvironment): Set<MessageResponse>{
        val user = dfe.getSource<User>() ?: throw  NullPointerException("Chat Not Found")

        val responseList: MutableSet<MessageResponse> = mutableSetOf()
        user.userMessages.forEach{ message ->
            responseList.add(
                MessageResponse(
                    messageSender = message.messageSender.userId,
                    messageBody = message.messageBody
                ))}

        return responseList
    }
}