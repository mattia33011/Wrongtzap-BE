package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.controller.web.response.UserProfile
import it.mag.wrongtzap.model.Chat
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
    fun getChats(dfe: DgsDataFetchingEnvironment): Set<Chat> {

        val user = dfe.getSource<User>() ?: throw UserNotFoundException()
        return user.chats
    }

    @DgsData(parentType = "User", field = "friends")
    fun getFriends(dfe: DgsDataFetchingEnvironment): Set<UserProfile>{
        val user = dfe.getSource<User>() ?: throw UserNotFoundException()

        return user.friends.map { friend -> UserProfile(
            userId = friend.userId,
            username = friend.username
        ) }.toSet()
    }
}