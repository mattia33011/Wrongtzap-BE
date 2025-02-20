package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInChat
import it.mag.wrongtzap.controller.web.chat.response.ChatDTO
import it.mag.wrongtzap.controller.web.chat.response.EntryDateDTO
import it.mag.wrongtzap.controller.web.page.response.PagedMessageResponse
import it.mag.wrongtzap.controller.web.user.response.ProfileResponse
import it.mag.wrongtzap.jwt.UserDetail
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.service.MapperService
import it.mag.wrongtzap.service.MessageService
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder

@DgsComponent
class ChatDataFetcher @Autowired constructor(
    private val chatService: ChatService,
    private val messageService: MessageService,
    private val userService: UserService,
    private val mapper: MapperService
) {
    @DgsQuery(field = "chat")
    fun getChat(@InputArgument chatId: String) = chatService.retrieveChatById(chatId)

    @DgsQuery(field = "everyChat")
    fun getEveryChat() = chatService.retrieveAllChats()


    @DgsData(parentType = "Chat", field = "members")
    fun getChatUsers(dfe: DataFetchingEnvironment):List<ProfileResponse>{
        val chat = dfe.getSource<ChatDTO>() ?: throw ChatNotFoundException()

//        val responseList: List<ProfileResponse> =
//            chat.members.map{ user ->
//                ProfileResponse(
//                    userId = user.userId,
//                    username = user.username
//                )
//            }.toList()

        return chat.members
    }

    @DgsData(parentType = "GroupChat", field = "members")
    fun getGroupUsers(dfe: DataFetchingEnvironment):Set<ProfileResponse>{
        val chat = dfe.getSource<GroupChat>() ?: throw ChatNotFoundException()

            val responseList: MutableSet<ProfileResponse> = mutableSetOf()

            chat.members.forEach{ user ->
                responseList.add(
                    ProfileResponse(
                    userId = user.userId,
                    username = user.username
            )
                )}

            return responseList
    }

    @DgsData(parentType = "GroupChat", field = "memberEntry")
    fun getJoinDate(dfe: DataFetchingEnvironment): List<EntryDateDTO>{
        val chat = dfe.getSource<GroupChat>() ?: throw ChatNotFoundException()
        return chat.userJoinEntry.map { EntryDateDTO(userId = it.key.toString(), it.value) }
    }

    @DgsData(parentType = "Chat", field = "messages")
    fun getChatMessages(dfe: DataFetchingEnvironment): PagedMessageResponse {

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as UserDetail
        val email = jwt.getId()
        val user: User = userService.retrieveByEmail(email) ?: throw UserNotFoundException()

        val chat = dfe.getSource<ChatDTO>() ?: throw ChatNotFoundException()
        chat.members.firstOrNull{ profile -> profile.userId == user.userId} ?: throw UserNotFoundInChat()

        val res = messageService.retrieveChatFilteredPage(userId = user.userId, chatId = chat.chatId)
        return PagedMessageResponse(
            content = res.content.map(mapper::messageToResponse),
            pageNumber = res.number,
            pageSize = res.size,
            totalPages = res.totalPages,
            totalRecords = res.totalElements
        )
    }

    @DgsData(parentType = "GroupChat", field = "messages")
    fun getGroupMessages(dfe: DataFetchingEnvironment): PagedMessageResponse {

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as UserDetail
        val email = jwt.getId()

        val chat = dfe.getSource<GroupChat>() ?: throw ChatNotFoundException()
        val user = chat.members.firstOrNull{ user -> user.email == email}
            ?: throw UserNotFoundInChat()

        val res = messageService.returnPage(userId =  user.userId, chatId = chat.chatId)
        return PagedMessageResponse(
            content = res.content.map(mapper::messageToResponse),
            pageNumber = res.number,
            pageSize = res.size,
            totalPages = res.totalPages,
            totalRecords = res.totalElements
        )

    }

    @DgsData(parentType = "Chat", field = "archived")
    fun getChatArchivedIds(dfe: DataFetchingEnvironment): List<String>{
        val chat = dfe.getSource<ChatDTO>()
            ?: throw ChatNotFoundException()

        return chat.archived
    }


    @DgsData(parentType = "GroupChat", field = "archived")
    fun getGroupArchivedIds(dfe: DataFetchingEnvironment): MutableList<String>{
        val chat = dfe.getSource<GroupChat>()
            ?: throw ChatNotFoundException()

        return chat.archived
    }

    @DgsData(parentType = "GroupChat", field = "admins")
    fun getGroupAdminIds(dfe: DataFetchingEnvironment): List<String>{
        val chat = dfe.getSource<GroupChat>()
            ?: throw ChatNotFoundException()

        return chat.admins.map {admin -> admin.userId}
    }


}