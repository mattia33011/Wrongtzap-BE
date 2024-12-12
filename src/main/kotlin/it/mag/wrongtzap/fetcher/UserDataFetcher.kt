package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.controller.web.response.user.UserProfileResponse
import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.service.MapperService
import it.mag.wrongtzap.service.UserService

import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class UserDataFetcher @Autowired constructor(
    private val userService: UserService,
    private val conversionService: MapperService,
) {

    @DgsQuery(field = "user")
    fun getUser(@InputArgument userId: String) = userService.retrieveById(userId)


    @DgsQuery(field = "everyUser")
    fun getAllUsers() = userService.retrieveAllUsers()


    @DgsData(parentType = "User", field = "directChats")
    fun getDirectChats(dfe: DgsDataFetchingEnvironment): Set<DirectChat> {

        val user = dfe.getSource<User>() ?: throw UserNotFoundException()
        return user.directChats
    }


    @DgsData(parentType = "User", field = "groupChats")
    fun getGroupChats(dfe: DgsDataFetchingEnvironment): Set<GroupChat> {

        val user = dfe.getSource<User>() ?: throw UserNotFoundException()
        return user.groupChats
    }

    @DgsData(parentType = "User", field = "friends")
    fun getFriends(dfe: DgsDataFetchingEnvironment): Set<UserProfileResponse>{
        val user = dfe.getSource<User>() ?: throw UserNotFoundException()

        return user.friends.map { friend -> UserProfileResponse(
            userId = friend.userId,
            username = friend.username
        ) }.toSet()
    }
}