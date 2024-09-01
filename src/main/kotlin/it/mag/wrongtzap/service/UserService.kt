package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import it.mag.wrongtzap.request.UserRequest
import jakarta.transaction.Transactional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    //Create method
    fun createUser(userRequest: UserRequest): User {
        val user = User(
            userName = userRequest.userName,
            userMail = userRequest.uSerMail,
            userPassword = passwordEncoder.encode(userRequest.userPassword)
        )

        return userRepository.save(user)
    }

    //Read methods
    fun retrieveByUsername(username: String) = userRepository.findByUserName(username)
    fun retrieveById(userId: String) = userRepository.findById(userId).orElseThrow { NullPointerException("User Not Found") }
    fun retrieveAllUsers() = userRepository.findAll()

    fun retrieveAllChats(userId:String): MutableSet<Chat>{
        val user = userRepository.findById(userId).orElseThrow{NullPointerException("User not Found")}
        return user.userChats
    }
    fun retrieveChat(userId: String, chatId: String): Chat{
        val user = userRepository.findById(userId).orElseThrow { NullPointerException("User Not Found") }
        val chat = user.userChats.firstOrNull { it.chatId==chatId }
            ?: throw NullPointerException("Chat Not Found")

        return chat
    }
    fun searchMessages(userId: String, messageBody: String): MutableList<Message>{
        val user = userRepository.findById(userId).orElseThrow{NullPointerException("User Not Found")}
        val messages: MutableList<Message> = mutableListOf()

        user.userChats.forEach{chat ->
            messages.addAll(chat.chatMessages.filter { it.messageBody.contains(messageBody) })
        }
        return messages
    }

    //Update method
    @Transactional
    fun leaveChat(userId: String, chatId: String){
        val user = userRepository.findById(userId).orElseThrow{NullPointerException("User Not Found")}
        user.userChats.removeIf{ it.chatId==chatId}
        userRepository.save(user)
    }
    @Transactional
    fun editUserName(userId: String, newName: String): User{
        val user: User = userRepository.findById(userId).orElseThrow { NullPointerException("User Not Found")
        }

        user.apply {
            userName = newName
        }

        return userRepository.save(user)
    }

    @Transactional
    fun editUserMail(userId: String, newMail: String): User{
        val user: User = userRepository.findById(userId).orElseThrow { NullPointerException("User Not Found")
        }

        user.apply {
            userMail = newMail
        }

        return userRepository.save(user)
    }


    //Delete method
    @Transactional
    fun deleteUser(userId: String): User{
        val user = userRepository.findById(userId).orElseThrow() {NullPointerException("User Not Found")}
        userRepository.deleteByUserNameAndUserMail(user.userName, user.userMail)
        return user
    }
}