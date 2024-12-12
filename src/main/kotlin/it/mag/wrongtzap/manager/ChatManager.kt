package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.exception.chat.InvalidChatnameFormatException
import it.mag.wrongtzap.controller.web.exception.chat.InvalidNumberOfParticipantsException
import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.request.chat.DirectChatRequest
import it.mag.wrongtzap.controller.web.request.chat.GroupChatRequest
import it.mag.wrongtzap.controller.web.request.message.MessageDeletionRequest
import it.mag.wrongtzap.controller.web.request.message.MessageRequest
import it.mag.wrongtzap.controller.web.response.chat.DirectChatResponse
import it.mag.wrongtzap.controller.web.response.chat.GroupChatResponse
import it.mag.wrongtzap.controller.web.response.message.MessageResponse
import it.mag.wrongtzap.controller.web.response.user.UserResponse
import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.model.type.ChatRequestType
import it.mag.wrongtzap.model.type.ChatResponseType
import it.mag.wrongtzap.service.*
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatManager @Autowired constructor(
    private val directChatService: DirectChatService,
    private val groupChatService: GroupChatService,
    private val userService: UserService,
    private val messageService: MessageService,
    private val mapperService: MapperService
) {
    private val chatNameFormat = Regex("^[\\w\\s]{1,100}\$")

    fun findChat(chatId: String, type: String): Any{
        return if (type == "group"){
            groupChatService.retrieveChatById(chatId)
        } else
            directChatService.retrieveChatById(chatId)
    }

    fun createChat(request: ChatRequestType): ChatResponseType{
        return when(request){
            is ChatRequestType.Direct-> ChatResponseType.Direct(createDirectChat(request.direct))
            is ChatRequestType.Group -> ChatResponseType.Group(createGroupChat(request.group))
        }
    }

    @Transactional
    fun createDirectChat(request: DirectChatRequest): DirectChatResponse {
        val firstUser = userService.retrieveById(request.firstUserId)
        val secondUser = userService.retrieveById(request.secondUserId)

        val chat = DirectChat(
            participants = listOf(firstUser,secondUser)
        )

        directChatService.saveChat(chat)
        return mapperService.directChatToResponse(chat)
    }

    @Transactional
    fun createGroupChat(request: GroupChatRequest): GroupChatResponse {
        if(!chatNameFormat.matches(request.chatName))
            throw InvalidChatnameFormatException()

        if(request.chatUsersIds.size <= 2)
            throw InvalidNumberOfParticipantsException()

        val admin = mutableSetOf<User>()
        admin.add(userService.retrieveById(request.firstUserId))

        val participants = request.chatUsersIds.map { id -> userService.retrieveById(id) }.toMutableSet()
        val joinDates = participants.map { user -> user.userId  }.associateWith { System.currentTimeMillis() }.toMutableMap()

        val chat = GroupChat(
            name = request.chatName,
            participants = participants,
            userJoinDates = joinDates,
            admins = admin
        )

        groupChatService.saveChat(chat)
        return mapperService.groupChatToResponse(chat)
    }

    @Transactional
    fun addUserToGroup(chatId: String, userId: String): UserResponse {

        val chat = groupChatService.retrieveChatById(chatId)

        val user = userService.retrieveById(userId)

        chat.apply {
            userJoinDates[userId] = System.currentTimeMillis()
            participants.add(user)
        }

        groupChatService.saveChat(chat)
        return mapperService.userToResponse(user)
    }


    @Transactional
    fun createMessage(request: MessageRequest, type: String): MessageResponse {

        val sender = userService.retrieveById(request.userId)

        val chat = if (type == "group")
            groupChatService.retrieveChatById(request.chatId)
        else
            directChatService.retrieveChatById(request.chatId)


        val message = Message(
            sender = sender,
            content = request.body,
            associatedChat = chat,
            timestamp = request.timestamp
        )

        messageService.saveMessage(message)
        return mapperService.messageToResponse(message)
    }


    @Transactional
    fun deleteMessageForSelf(request: MessageDeletionRequest){

        val chat = if(request.type == "group")
            groupChatService.retrieveChatById(request.chatId)
        else
            directChatService.retrieveChatById(request.chatId)

        val message = chat.messages.firstOrNull{ it.messageId == request.messageId}
            ?: throw MessageNotFoundException()


        message.apply {
            deletedForUser.add(request.userId)
        }

        messageService.saveMessage(message)
    }

    @Transactional
    fun deleteMessageForEveryone(request: MessageDeletionRequest){
        val chat = if(request.type== "group")
            groupChatService.retrieveChatById(request.chatId)
        else
            directChatService.retrieveChatById(request.chatId)

        val message = chat.messages.firstOrNull{ it.messageId == request.messageId}
            ?: throw MessageNotFoundException()

        message.deleteForEveryone()
        messageService.saveMessage(message)
    }

}