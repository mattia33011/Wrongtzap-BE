package it.mag.wrongtzap.service

import cn.hutool.core.lang.Snowflake
import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.user.request.EditProfileRequest
import it.mag.wrongtzap.controller.web.user.request.PendingFriendRequest
import it.mag.wrongtzap.controller.web.user.request.UpdateFriendRequest
import it.mag.wrongtzap.controller.web.user.response.FriendResponse
import it.mag.wrongtzap.controller.web.user.response.UserResponse
import it.mag.wrongtzap.model.*
import it.mag.wrongtzap.repository.FriendRepository
import it.mag.wrongtzap.repository.UserRepository
import it.mag.wrongtzap.util.enums.FriendStatus
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val mapper: MapperService,
    private val snowflake: Snowflake
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

    fun retrieveChat(userId: String, chatId: String): Chat{

        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User not found")
        }

        val chat = user.chats.firstOrNull { it.chatId==chatId }
            ?: throw ChatNotFoundException("Chat not found")

        return chat
    }

    fun retrieveGroup(userId: String, chatId: String): GroupChat{

        val user = userRepository.findById(userId).orElseThrow {
            UserNotFoundException("User not found")
        }

        val chat = user.groups.firstOrNull { it.chatId==chatId }
            ?: throw ChatNotFoundException("Chat not found")

        return chat
    }

    fun searchMessages(userId: String, messageBody: String): MutableList<Message>{

        val user = userRepository.findById(userId)
            .orElseThrow{ UserNotFoundException("User Does not exist") }

        val messages: MutableList<Message> = mutableListOf()

        user.chats.forEach{ chat ->
            messages.addAll( chat.messages.filter { it.content.contains(messageBody) })
        }


        user.groups.forEach{ chat ->
            messages.addAll( chat.messages.filter { it.content.contains(messageBody) })
        }

        return messages.ifEmpty {
            throw MessageNotFoundException("")
        }
    }

    @Transactional
    fun sendFriendRequest(request: PendingFriendRequest): Pair<FriendResponse, FriendResponse>{
        val user = userRepository.findById(request.senderId)
            .orElseThrow { UserNotFoundException("User not found") }
        val receiver = userRepository.findById(request.receiverId)
            .orElseThrow { UserNotFoundException("Friend not found") }

        val friend = Friend(
            requestId = snowflake.nextId().toString(),
            sender = user,
            receiver = receiver,
            status = FriendStatus.PENDING
        )

        friendRepository.save(friend)

        val userResponse = mapper.sentFriendToResponse(friend)
        val receiverResponse = mapper.receivedFriendToResponse(friend)

        return userResponse to receiverResponse
    }

    fun acceptRequest(request: UpdateFriendRequest): Pair<FriendResponse, FriendResponse>{


        val friend = friendRepository.findById(request.friendshipId).orElseThrow { NullPointerException() }

        friend.status = FriendStatus.ACCEPTED
        friendRepository.save(friend)

        val userResponse = mapper.sentFriendToResponse(friend)
        val receiverResponse = mapper.receivedFriendToResponse(friend)

        return userResponse to receiverResponse
    }

    fun rejectRequest(request: UpdateFriendRequest): String{


        val friend = friendRepository.findById(request.friendshipId).orElseThrow{NullPointerException()}
        friendRepository.delete(friend)
        return request.friendshipId
    }
//
//    @Transactional
//    fun removeFriend(request: PendingFriendRequest): Pair<ProfileResponse, ProfileResponse>{
//        val sender = userRepository.findById(request.senderId).orElseThrow { UserNotFoundException() }
//        val receiver = userRepository.findById(request.receiverId).orElseThrow { UserNotFoundException() }
//
//
//        val friendship = sender.friends.find { friend -> friend.userId == receiver.userId }
//            ?: throw UserNotFoundException()
//
//        val reverseFriendship = receiver.friends.find { friend -> friend.userId == sender.userId }
//            ?: throw UserNotFoundException()
//
//        sender.friends.remove(friendship)
//        receiver.friends.remove(reverseFriendship)
//
//        userRepository.save(sender)
//        userRepository.save(receiver)
//
//        return Pair(
//            first = mapper.userToProfile(sender),
//            second = mapper.userToProfile(receiver)
//        )
//    }

    @Transactional
    fun editUserName(request: EditProfileRequest): UserResponse {

        val user = userRepository.findById(request.userId).getOrNull()
            ?: throw UserNotFoundException()

        if(!request.username.isNullOrEmpty())
            user.apply {
                username = request.username
            }


        userRepository.save(user)

        return mapper.userToResponse(user)
    }

    //Delete method
    @Transactional
    fun deleteUser(userId: String, email: String){
        userRepository.deleteByUserIdAndEmail(userId, email)
    }
}