package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import jakarta.transaction.Transactional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository
) {
    //Create method
    fun createUser(user: User) = userRepository.save(user)

    //Read methods
    fun retrieveByUsername(username: String) = userRepository.findByUserName(username)
    fun retrieveById(userId: String) = userRepository.findById(userId)
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
    fun editUserName(userId: String, newUserName: String): User{
        val oldCredentials: User = userRepository.findById(userId).orElseThrow { NullPointerException("User Not Found")
        }

        oldCredentials.apply {
            userName = newUserName
        }

        return userRepository.save(oldCredentials)
    }

    @Transactional
    fun editUserMail(userId: String, newUserMail: String): User{
        val oldCredentials: User = userRepository.findById(userId).orElseThrow { NullPointerException("User Not Found")
        }

        oldCredentials.apply {
            userMail = newUserMail
        }

        return userRepository.save(oldCredentials)
    }

    //Delete method
    @Transactional
    fun deleteUser(userId: String): String{
        val user = userRepository.findById(userId).orElseThrow() {NullPointerException("User Not Found")}
        userRepository.deleteByUserIdAndUserMail(user.userId, user.userMail)
        return user.userName
    }
}