package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.page.request.PageChatRequest
import it.mag.wrongtzap.controller.web.page.response.base.PagedChat
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.ChatRepository
import it.mag.wrongtzap.util.specification.ChatSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class ChatService @Autowired constructor(
    private val chatRepository: ChatRepository,
){

    fun saveChat(chat: Chat) = chatRepository.save(chat)

    fun retrieveChatById(chatId: String): Chat = chatRepository.findById(chatId)
        .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

    fun retrieveAllChats() = chatRepository.findAll()

    fun returnFirstPage(user: User): Page<Chat>{
        val page = PageRequest.of(0, 15)
        return chatRepository.findByMembersContains(user, page)
    }

    fun returnPage(request: PageChatRequest): PagedChat {
        val page = PageRequest.of(
            request.pageNumber.toInt(),
            request.pageSize.toInt(),
            Sort.by("creationDate").descending()
        )
        val spec = ChatSpecification.containsUser(request.userId)
        val chat = chatRepository.findAll(spec, page)

        return PagedChat(
            content = chat.content,
            pageNumber = chat.number,
            pageSize = chat.size,
            totalPages = chat.totalPages,
            totalRecords = chat.totalElements,
        )
    }

//    @Transactional
//    fun editMessage(chatId: String, messageId: String, newBody:String){
//        val chat = chatRepository.findById(chatId)
//            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }
//
//        chat.messages.first{ it.messageId == messageId}.apply { content = newBody }
//        chatRepository.save(chat)
//    }
}