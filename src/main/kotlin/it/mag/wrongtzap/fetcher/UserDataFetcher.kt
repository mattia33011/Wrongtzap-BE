package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.controller.web.response.ChatResponse
import it.mag.wrongtzap.service.Mapper
import it.mag.wrongtzap.service.UserService

import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class UserDataFetcher @Autowired constructor(
    private val userService: UserService,
    private val conversionService: Mapper,
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
                responseList.add(conversionService.chatToResponse(chat))
            }

            return responseList
    }

}