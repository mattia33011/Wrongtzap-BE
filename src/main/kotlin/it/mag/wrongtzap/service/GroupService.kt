package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.page.request.PageChatRequest
import it.mag.wrongtzap.controller.web.chat.request.ParticipantRequest
import it.mag.wrongtzap.controller.web.page.response.base.PagedGroup
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.GroupRepository
import it.mag.wrongtzap.util.specification.GroupSpecification

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class GroupService @Autowired constructor(
    private val groupChatRepository: GroupRepository,
    private val mapperService: MapperService,
) {

    //
    // --  Create methods --
    //

    fun saveChat(chat: GroupChat) = groupChatRepository.save(chat)


    //
    //Retrieve Methods
    //

    fun retrieveChatById(chatId: String): GroupChat = groupChatRepository.findById(chatId)
        .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

    fun retrieveAllChats() = groupChatRepository.findAll()

    fun returnFirstPage(user: User): Page<GroupChat>{
        val page = PageRequest.of(0, 15)
        return groupChatRepository.findByMembersContains(user, page)
    }

    fun returnPage(request: PageChatRequest): PagedGroup {
        val page = PageRequest.of(
            request.pageNumber.toInt(),
            request.pageSize.toInt(),
            Sort.by("creationDate").descending()
        )
        val spec = GroupSpecification.containsUser(request.userId)
        val groups = groupChatRepository.findAll(spec,page)

        return PagedGroup(
            content = groups.content,
            pageNumber = groups.number,
            pageSize = groups.size,
            totalPages = groups.totalPages,
            totalRecords = groups.totalElements,
        )
    }

    @Transactional
    fun editChatName(chatId: String, newName: String){
        val chat = groupChatRepository.findById(chatId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.apply {
            name = newName
        }

        groupChatRepository.save(chat)
    }

    @Transactional
    fun leaveGroup(request: ParticipantRequest): GroupChat {
        val chat = groupChatRepository.findById(request.chatId)
            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.members.removeIf { it.userId==request.userId }
        chat.userJoinEntry.remove(request.userId)
        return groupChatRepository.save(chat)
    }

    @Transactional
    fun removeUser(request: ParticipantRequest): GroupChat {
        val chat = groupChatRepository.findById(request.chatId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.members.removeIf{ it.userId== request.userId}
        chat.userJoinEntry.remove(request.userId)
        return groupChatRepository.save(chat)
    }

//    @Transactional
//    fun editMessage(chatId: String, messageId: String, newBody:String){
//        val chat = groupChatRepository.findById(chatId)
//            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }
//
//        chat.messages.first{ it.messageId == messageId}.apply { content = newBody }
//        groupChatRepository.save(chat)
//    }




    //
    // Delete Methods
    //

    @Transactional
    fun deleteChat(chatId: String) = groupChatRepository.deleteById(chatId)

}