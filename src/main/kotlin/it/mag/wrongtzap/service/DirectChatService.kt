package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.repository.DirectChatRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DirectChatService @Autowired constructor(
    private val chatRepository: DirectChatRepository,
){

    fun saveChat(chat: DirectChat) = chatRepository.save(chat)

    fun retrieveChatById(chatId: String): DirectChat = chatRepository.findById(chatId)
        .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

    fun retrieveAllChats() = chatRepository.findAll()


//    @Transactional
//    fun editMessage(chatId: String, messageId: String, newBody:String){
//        val chat = chatRepository.findById(chatId)
//            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }
//
//        chat.messages.first{ it.messageId == messageId}.apply { content = newBody }
//        chatRepository.save(chat)
//    }
}