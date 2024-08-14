package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.UserService
import it.mag.wrongtzap.model.Chat
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class UserDataFetcher@Autowired constructor(
   val userService: UserService
) {
    @DgsQuery
    fun getUser(@InputArgument userId: String) = userService.retrieveById(userId)

    @DgsQuery
    fun getAllUsers() = userService.retrieveAllUsers()
}