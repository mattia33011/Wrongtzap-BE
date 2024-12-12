package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.repository.GroupChatRepository

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupChatService @Autowired constructor(
    private val groupChatRepository: GroupChatRepository
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
    fun leaveGroup(chatId: String, userId: String): GroupChat {
        val chat = groupChatRepository.findById(chatId)
            .orElseThrow { it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.participants.removeIf { it.userId==userId }
        chat.userJoinDates.remove(userId)
        return groupChatRepository.save(chat)
    }

    @Transactional
    fun removeUser(chatId: String, userId: String): GroupChat {
        val chat = groupChatRepository.findById(chatId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException() }

        chat.participants.removeIf{ it.userId== userId}
        chat.userJoinDates.remove(userId)
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