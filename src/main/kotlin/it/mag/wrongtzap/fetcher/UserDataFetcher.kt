package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import graphql.schema.DataFetchingEnvironment
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


    @DgsData(parentType = "User", field = "chats")
    fun getChats(dfe: DgsDataFetchingEnvironment): Set<ChatResponse> {

        val user = dfe.getSource<User>() ?: throw NullPointerException("User Not Found")

            val responseList: MutableSet<ChatResponse> = mutableSetOf()

            user.chats.forEach { chat ->
                responseList.add(
                    ChatResponse(
                        chatName = chat.name,
                        chatId = chat.chatId
                    )
                )
            }

            return responseList
    }

}