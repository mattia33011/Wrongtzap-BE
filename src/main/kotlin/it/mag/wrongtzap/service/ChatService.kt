package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.repository.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatService @Autowired constructor(
    private val chatRepository: ChatRepository,
){

    //
    // --  Create methods --
    //

    fun saveChat(chat: Chat) = chatRepository.save(chat)

    
    //
    //Retrieve Methods
    //

    fun retrieveChatById(chatId: String): Chat = chatRepository.findById(chatId)
        .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

    fun retrieveAllChats() = chatRepository.findAll()


    @Transactional
    fun editChatName(chatId: String, newName: String){
        val chat = chatRepository.findById(chatId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.apply {
            name = newName
        }

        chatRepository.save(chat)
    }

    @Transactional
    fun leaveGroup(chatId: String, userId: String): Chat{
        val chat = chatRepository.findById(chatId)
            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.participants.removeIf { it.userId==userId }
        chat.userJoinDates.remove(userId)
        return chatRepository.save(chat)
    }

    @Transactional
    fun removeUser(chatId: String, userId: String): Chat{
        val chat = chatRepository.findById(chatId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.participants.removeIf{ it.userId== userId}
        chat.userJoinDates.remove(userId)
        return chatRepository.save(chat)
    }

    @Transactional
    fun editMessage(chatId: String, messageId: String, newBody:String){
        val chat = chatRepository.findById(chatId)
            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.messages.first{ it.messageId == messageId}.apply { content = newBody }
        chatRepository.save(chat)
    }




    //
    // Delete Methods
    //

    @Transactional
    fun deleteChat(chatId: String) = chatRepository.deleteById(chatId)

}