package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.request.user.FriendRequest
import it.mag.wrongtzap.controller.web.response.user.UserResponse
import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.GroupChat
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
    private val mapper: MapperService
) {
    //Create method
    fun saveUser(user: User) = userRepository.save(user)


    //Read methods
    fun retrieveByUsername(username: String) = userRepository.findByUsername(username)
    fun retrieveById(userId: String): User {
        val user = userRepository.findById(userId).orElseThrow{
            UserNotFoundException()
        }
        return user
    }

    fun retrieveAllUsers() = userRepository.findAll()
    fun retrieveByEmail(userMail: String) = userRepository.findByEmail(userMail.lowercase())
    fun retrieveByPasswordAndEmail(userPassword: String, userMail: String) = userRepository.findByPasswordAndEmail(userPassword, userMail)

    fun retrieveChat(userId: String, chatId: String): DirectChat{

        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User not found")
        }

        val chat = user.directChats.firstOrNull { it.chatId==chatId }
            ?: throw ChatNotFoundException("Chat not found")

        return chat
    }

    fun retrieveGroup(userId: String, chatId: String): GroupChat{

        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User not found")
        }

        val chat = user.groupChats.firstOrNull { it.chatId==chatId }
            ?: throw ChatNotFoundException("Chat not found")

        return chat
    }

    fun searchMessages(userId: String, messageBody: String): MutableList<Message>{

        val user = userRepository.findById(userId)
            .orElseThrow{ UserNotFoundException("User Does not exist") }

        val messages: MutableList<Message> = mutableListOf()

        user.directChats.forEach{ chat ->
            messages.addAll( chat.messages.filter { it.content.contains(messageBody) })
        }


        user.groupChats.forEach{ chat ->
            messages.addAll( chat.messages.filter { it.content.contains(messageBody) })
        }

        return messages.ifEmpty {
            throw MessageNotFoundException("")
        }
    }

    @Transactional
    fun addFriend(request: FriendRequest): Pair<UserResponse, UserResponse>{
        val sender = userRepository.findById(request.senderId).orElseThrow { UserNotFoundException() }
        val receiver = userRepository.findById(request.receiverId).orElseThrow { UserNotFoundException() }

        sender.friends.add(receiver)
        userRepository.save(sender)

        return Pair(
            first = mapper.userToResponse(sender),
            second = mapper.userToResponse(receiver)
        )
    }

    @Transactional
    fun removeFriend(request: FriendRequest): Pair<UserResponse, UserResponse>{
        val sender = userRepository.findById(request.senderId).orElseThrow { UserNotFoundException() }
        val receiver = userRepository.findById(request.receiverId).orElseThrow { UserNotFoundException() }

        sender.friends.remove(receiver)
        userRepository.save(sender)

        return Pair(
            first = mapper.userToResponse(sender),
            second = mapper.userToResponse(receiver)
        )
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