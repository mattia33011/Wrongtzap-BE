package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.request.user.EditProfileRequest
import it.mag.wrongtzap.controller.web.request.user.FriendRequest
import it.mag.wrongtzap.controller.web.request.user.UserDeleteRequest
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse
import it.mag.wrongtzap.model.*
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
        return userRepository.findById(userId).orElseThrow{ UserNotFoundException() }
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
    fun addFriend(request: FriendRequest): Pair<ProfileResponse, ProfileResponse>{

            val user = userRepository.findById(request.senderId)
                .orElseThrow { UserNotFoundException("User not found") }
            val receiver = userRepository.findById(request.receiverId)
                .orElseThrow { UserNotFoundException("Friend not found") }

        user.friends.add(receiver.userId)
        receiver.friends.add(user.userId)

        // Save both users
        userRepository.save(user)
        userRepository.save(receiver)

            return Pair(
                first = mapper.userToProfile(user),
                second = mapper.userToProfile(receiver)
            )
    }

    @Transactional
    fun removeFriend(request: FriendRequest): Pair<ProfileResponse, ProfileResponse>{
        val sender = userRepository.findById(request.senderId).orElseThrow { UserNotFoundException() }
        val receiver = userRepository.findById(request.receiverId).orElseThrow { UserNotFoundException() }


        val friendship = sender.friends.find { friendId -> friendId == receiver.userId }
            ?: throw UserNotFoundException()

        val reverseFriendship = receiver.friends.find { friendId -> friendId == sender.userId }
            ?: throw UserNotFoundException()

        sender.friends.remove(friendship)
        receiver.friends.remove(reverseFriendship)

        userRepository.save(sender)
        userRepository.save(receiver)

        return Pair(
            first = mapper.userToProfile(sender),
            second = mapper.userToProfile(receiver)
        )
    }

    @Transactional
    fun editUserName(request: EditProfileRequest): User{

        val user = userRepository.findById(request.userId).getOrNull()
            ?: throw UserNotFoundException()

        if(!request.username.isNullOrEmpty())
            user.apply {
                username = request.username
            }

        return userRepository.save(user)
    }

    //Delete method
    @Transactional
    fun deleteUser(userId: String, email: String){
        userRepository.deleteByUserIdAndEmail(userId, email)
    }
}