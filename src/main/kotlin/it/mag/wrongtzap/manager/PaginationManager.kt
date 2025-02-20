package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.page.request.PageMessageRequest
import it.mag.wrongtzap.controller.web.page.request.PageChatRequest
import it.mag.wrongtzap.controller.web.page.response.PagedChatResponse
import it.mag.wrongtzap.controller.web.page.response.PagedGroupResponse
import it.mag.wrongtzap.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PaginationManager @Autowired constructor(
    private val groupService: GroupService,
    private val chatService: ChatService,
    private val messageService: MessageService,
    private val mapper: MapperService
) {
    fun nextGroupPage(request: PageChatRequest): PagedGroupResponse {
        val groupPage = groupService.returnPage(request)
        val mappedGroups = groupPage.content.map{ group ->
            val messageRequest = PageMessageRequest(
                userId = request.userId,
                chatId = group.chatId,
                pageSize = request.pageSize,
                pageNumber = request.pageNumber,
            )
            val messages = messageService.returnPage(messageRequest)
            mapper.groupToPagedGroup(group, messages)
        }
        return PagedGroupResponse(
            pageNumber = groupPage.pageNumber,
            pageSize = groupPage.pageSize,
            totalPages = groupPage.totalPages,
            totalRecords = groupPage.totalRecords,
            content = mappedGroups
        )
    }

    fun nextChatPage(request: PageChatRequest): PagedChatResponse {
        val chatPage = chatService.returnPage(request)
        val mappedChats = chatPage.content.map{ chat ->
            val messageRequest = PageMessageRequest(
                userId = request.userId,
                chatId = chat.chatId,
                pageSize = request.pageSize,
                pageNumber = request.pageNumber,
            )
            val messages = messageService.returnPage(messageRequest)
            mapper.chatToPagedChat(chat, messages)
        }
        return PagedChatResponse(
            pageNumber = chatPage.pageNumber,
            pageSize = chatPage.pageSize,
            totalPages = chatPage.totalPages,
            totalRecords = chatPage.totalRecords,
            content = mappedChats
        )
    }
}