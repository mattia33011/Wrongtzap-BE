package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.*
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.page.response.PagedGraphChat
import it.mag.wrongtzap.controller.web.page.response.PagedGraphGroup
import it.mag.wrongtzap.controller.web.user.response.FriendResponse
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.service.GroupService
import it.mag.wrongtzap.service.MapperService
import it.mag.wrongtzap.service.UserService

import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class UserDataFetcher @Autowired constructor(
    private val userService: UserService,
    private val chatService: ChatService,
    private val groupService: GroupService,
    private val mapper: MapperService,
) {

    @DgsQuery(field = "user")
    fun getUser(@InputArgument userId: String) = userService.retrieveById(userId)


    @DgsQuery(field = "everyUser")
    fun getAllUsers() = userService.retrieveAllUsers()


    @DgsData(parentType = "User", field = "chats")
    fun pageChats(dfe: DgsDataFetchingEnvironment): PagedGraphChat {
        val user = dfe.getSource<User>() ?: throw UserNotFoundException()
        val page = chatService.returnFirstPage(user)

        return PagedGraphChat(
            pageNumber = page.number,
            pageSize = page.size,
            totalRecords = page.totalElements,
            totalPages = page.totalPages,
            content = page.content.map (mapper::chatToResponse)
        )
    }


    @DgsData(parentType = "User", field = "groups")
    fun pageGroups(dfe: DgsDataFetchingEnvironment): PagedGraphGroup {
        val user = dfe.getSource<User>() ?: throw UserNotFoundException()
        val page = groupService.returnFirstPage(user)

        return PagedGraphGroup(
            pageNumber = page.number,
            pageSize = page.size,
            totalRecords = page.totalElements,
            totalPages = page.totalPages,
            content = page.content.map(mapper::groupToResponse)
        )
    }

    @DgsData(parentType = "User", field = "friends")
    fun getFriends(dfe: DgsDataFetchingEnvironment): MutableSet<FriendResponse>{
        val user = dfe.getSource<User>() ?: throw UserNotFoundException()
        val friends: MutableSet<FriendResponse> = mutableSetOf()

        friends.addAll(user.inboundFriendships.map(mapper::receivedFriendToResponse))
        friends.addAll(user.outboundFriendships.map(mapper::sentFriendToResponse))

        return friends
    }

}