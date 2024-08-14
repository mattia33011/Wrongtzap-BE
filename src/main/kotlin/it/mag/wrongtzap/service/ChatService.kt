package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
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
    fun createChat(chat: Chat) = chatRepository.save(chat)

    @Transactional
    fun addChatUser(chatId: String, user: User): Chat{
        val chat = chatRepository.findById(chatId).orElseThrow{NullPointerException("Chat not found")}

        chat.apply {
            chatParticipants.add(user)
        }

        return chatRepository.save(chat)
    }


    @Transactional
    fun addChatMessage(chatId: String, message: Message): Chat{
        val chat = chatRepository.findById(chatId).orElseThrow{NullPointerException("Chat not found")}

        chat.apply {
            chatMessages.add(message)
        }

        return chatRepository.save(chat)
    }


    //
    //Retrieve Methods
    //

    fun retrieveChat(chatId: String) = chatRepository.findById(chatId)
    fun retrieveAllChats() = chatRepository.findAll()


    fun retrieverChatUser(chatId: String, userId: String): User {
        val chat = chatRepository.findById(chatId).orElseThrow { NullPointerException("Chat Not Found") }
        return chat.chatParticipants.firstOrNull() { it.userId==userId }
            ?: throw NullPointerException("User does not exist")
    }
    fun retrieveAllChatUsers(chatId: String): MutableSet<User>{
        val chat = chatRepository.findById(chatId).orElseThrow{NullPointerException("Chat Not Found")}
        return chat.chatParticipants
    }



    fun retrieveAllChatMessages(chatId: String): MutableList<Message>{
        val chat = chatRepository.findById(chatId).orElseThrow { NullPointerException("Chat Not Found") }
        return chat.chatMessages
    }
    fun searchChatMessages(chatId: String, body: String): List<Message>{
        val chat = chatRepository.findById(chatId).orElseThrow { NullPointerException("Chat Not Found") }
        return chat.chatMessages.filter{ it.messageBody.contains(body)}

    }

    //
    //Update Methods
    //


    @Transactional
    fun editChatName(chatId: String, newName: String){
        val chat = chatRepository.findById(chatId).orElseThrow{NullPointerException("Chat not found")}

        chat.apply {
            chatName = newName
        }

        chatRepository.save(chat)
    }

    @Transactional
    fun removeChatUser(chatId: String, userId: String){
        val chat = chatRepository.findById(chatId).orElseThrow { NullPointerException("Chat Not Found") }
        chat.chatParticipants.removeIf { it.userId== userId }
        chatRepository.save(chat)
    }

    @Transactional
    fun editChatMessage(chatId: String, messageId: Long, newBody:String){
        val chat = chatRepository.findById(chatId).orElseThrow { NullPointerException("Chat Not Found") }
        chat.chatMessages.first{ it.messageId == messageId}.apply { messageBody = newBody }
        chatRepository.save(chat)
    }

    //
    // Delete Methods
    //

    @Transactional
    fun deleteChat(chatId: String) = chatRepository.deleteById(chatId)

}