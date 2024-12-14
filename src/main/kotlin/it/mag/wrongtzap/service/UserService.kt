package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.request.user.FriendRequest
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
    fun addFriend(request: FriendRequest): Pair<ProfileResponse, ProfileResponse>{

            val user = userRepository.findById(request.senderId)
                .orElseThrow { UserNotFoundException("User not found") }
            val receiver = userRepository.findById(request.receiverId)
                .orElseThrow { UserNotFoundException("Friend not found") }

        user.friends.add(ProfileResponse(
                userId = receiver.userId,
                username = receiver.username
        ))
        receiver.friends.add(ProfileResponse(
            userId = user.userId,
            username = user.username
        ))

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


        val friendship = sender.friends.find { friend -> friend.userId == receiver.userId }
            ?: throw UserNotFoundException()

        val reverseFriendship = receiver.friends.find { friend -> friend.userId == sender.userId }
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