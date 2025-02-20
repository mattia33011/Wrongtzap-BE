package it.mag.wrongtzap.manager

import cn.hutool.core.lang.Snowflake
import it.mag.wrongtzap.controller.web.exception.chat.InvalidChatnameFormatException
import it.mag.wrongtzap.controller.web.exception.chat.InvalidNumberOfParticipantsException
import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.chat.request.ChatRequest
import it.mag.wrongtzap.controller.web.chat.request.GroupRequest
import it.mag.wrongtzap.controller.web.message.MessageDeletionRequest
import it.mag.wrongtzap.controller.web.message.MessageRequest
import it.mag.wrongtzap.controller.web.chat.response.ChatDTO
import it.mag.wrongtzap.controller.web.chat.response.GroupChatDTO
import it.mag.wrongtzap.controller.web.chat.request.ParticipantRequest
import it.mag.wrongtzap.controller.web.message.MessageResponse
import it.mag.wrongtzap.controller.web.user.response.UserResponse
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.model.type.ChatRequestType
import it.mag.wrongtzap.service.*
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatManager @Autowired constructor(
    private val directChatService: ChatService,
    private val groupChatService: GroupService,
    private val userService: UserService,
    private val snowflake: Snowflake,
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

    fun createChat(request: ChatRequestType): Any {
        return when(request){
            is ChatRequestType.Direct-> createChat(request.direct)
            is ChatRequestType.Group -> createGroup(request.group)
        }
    }

    @Transactional
    fun createChat(request: ChatRequest): ChatDTO {
        val firstUser = userService.retrieveById(request.firstUserId)
        val secondUser = userService.retrieveById(request.secondUserId)

        val chat = Chat(
            members = listOf(firstUser,secondUser)
        )

        chat.chatId = snowflake.nextId().toString()

        directChatService.saveChat(chat)
        return mapperService.chatToResponse(chat)
    }

    @Transactional
    fun createGroup(request: GroupRequest): GroupChatDTO {
        if(!chatNameFormat.matches(request.name))
            throw InvalidChatnameFormatException()

        if(request.userIds.size <= 2)
            throw InvalidNumberOfParticipantsException()

        val admin = mutableSetOf<User>()
        admin.add(userService.retrieveById(request.adminId))

        val participants = request.userIds.map { id -> userService.retrieveById(id) }.toMutableSet()
        val joinDates = participants.map { user -> user.userId  }.associateWith { System.currentTimeMillis() }.toMutableMap()

        val group = GroupChat(
            name = request.name,
            members = participants,
            userJoinEntry = joinDates,
            admins = admin
        )

        group.chatId = snowflake.nextId().toString()

        groupChatService.saveChat(group)
        return mapperService.groupToResponse(group)
    }

    @Transactional
    fun addUserToGroup(request: ParticipantRequest): UserResponse {

        val chat = groupChatService.retrieveChatById(request.chatId)

        val user = userService.retrieveById(request.userId)

        chat.apply {
            userJoinEntry[request.userId] = System.currentTimeMillis()
            members.add(user)
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
            messageId = snowflake.nextId().toString(),
            content = request.body,
            associatedChat = chat,
            timestamp = System.currentTimeMillis()
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