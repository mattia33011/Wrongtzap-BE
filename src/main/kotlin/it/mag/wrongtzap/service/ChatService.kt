package it.mag.wrongtzap.service

import it.mag.wrongtzap.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.ChatRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatService @Autowired constructor(
    private val chatRepository: ChatRepository,
){

    //
    // --  Create methods --
    //

    fun saveChat(chat: Chat) = chatRepository.save(chat)

    @Transactional
    fun addUserToChat(chatId: String, user: User): Chat{
        val chat = chatRepository.findById(chatId)
            .orElseThrow{ ChatNotFoundException() }

        chat.apply {
            userJoinDates.put(user, LocalDateTime.now())
            participants.add(user)
        }

        return chatRepository.save(chat)
    }

    
    //
    //Retrieve Methods
    //

    fun retrieveChatById(chatId: String) = chatRepository.findById(chatId)
        .orElseThrow { ChatNotFoundException() }

    fun retrieveAllChats() = chatRepository.findAll()


    @Transactional
    fun editChatName(chatId: String, newName: String){
        val chat = chatRepository.findById(chatId)
            .orElseThrow{ ChatNotFoundException() }

        chat.apply {
            name = newName
        }

        chatRepository.save(chat)
    }

    @Transactional
    fun leaveGroup(chatId: String, userMail: String): Chat{
        val chat = chatRepository.findById(chatId)
            .orElseThrow { ChatNotFoundException() }

        chat.participants.removeIf { it.email==userMail }
        return chatRepository.save(chat)
    }

    @Transactional
    fun removeUser(chatId: String, userId: String): Chat{
        val chat = chatRepository.findById(chatId)
            .orElseThrow{ ChatNotFoundException()}

        chat.participants.removeIf{ it.userId== userId}
        return chatRepository.save(chat)
    }

    @Transactional
    fun editMessage(chatId: String, messageId: String, newBody:String){
        val chat = chatRepository.findById(chatId)
            .orElseThrow { ChatNotFoundException() }

        chat.messages.first{ it.messageId == messageId}.apply { content = newBody }
        chatRepository.save(chat)
    }


    //
    // Delete Methods
    //

    @Transactional
    fun deleteChat(chatId: String) = chatRepository.deleteById(chatId)

}