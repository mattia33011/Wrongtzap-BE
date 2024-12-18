package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository,
) {


    //Create method
    fun saveUser(user: User) = userRepository.save(user)


    //Read methods
    fun retrieveByUsername(username: String) = userRepository.findByUsername(username)
    fun retrieveById(userId: String): User {
        val user = userRepository.findById(userId).orElseThrow{
            it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException()
        }
        return user
    }

    fun retrieveAllUsers() = userRepository.findAll()
    fun retrieveByEmail(userMail: String) = userRepository.findByEmail(userMail.lowercase())
    fun retrieveByPasswordAndEmail(userPassword: String, userMail: String) = userRepository.findByPasswordAndEmail(userPassword, userMail)

    fun retrieveChat(userId: String, chatId: String): Chat{

        val user = userRepository.findById(userId).orElseThrow {
            it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException("User not found")
        }

        val chat = user.chats.firstOrNull { it.chatId==chatId }
            ?: throw it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException("Chat not found")

        return chat
    }

    fun searchMessages(userId: String, messageBody: String): MutableList<Message>{

        val user = userRepository.findById(userId)
            .orElseThrow{ UserNotFoundException("User Does not exist") }

        val messages: MutableList<Message> = mutableListOf()

        user.chats.forEach{ chat ->
            messages.addAll(chat.messages.filter { it.content.contains(messageBody) })
        }

        return messages.ifEmpty {
            throw MessageNotFoundException("")
        }
    }


    @Transactional
    fun editUserName(userId: String, newName: String): User{

        val user = userRepository.findById(userId).getOrNull()
            ?: throw UserNotFoundException()

        user.apply {
            username = newName
        }

        return userRepository.save(user)
    }

    //Delete method
    @Transactional
    fun deleteUser(userId: String): User{
        val user = userRepository.findById(userId).getOrNull()
            ?: throw UserNotFoundException()

        userRepository.deleteByUsernameAndEmail(user.username, user.email)
        return user
    }
}