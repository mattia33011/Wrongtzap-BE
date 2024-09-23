package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException
import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.request.*
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.*
import it.mag.wrongtzap.util.EmailCoroutineScope
import jakarta.transaction.Transactional
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class UserManager @Autowired constructor(
    private val messageService: MessageService,
    private val userService: UserService,
    private val chatService: ChatService,
    private val emailService: EmailService,

    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil,

) {
    private val passwordFormat = Regex("^[\\w+_!()?*\\-\\[\\]{}]{8,20}$")
    private val usernameFormat = Regex("\\w{6,20}")
    private val emailFormat = Regex("^\\w+@[a-zA-Z_]+\\.[a-zA-Z]{2,}$")
    private val chatNameFormat = Regex("^[\\w\\s]{1,100}\$")

    //Create method
    fun createUser(userRequest: UserRequest): User {

        //Checks for invalid credentials format
        if(!passwordFormat.matches(userRequest.userPassword))
            throw it.mag.wrongtzap.controller.web.exception.user.InvalidPasswordFormatException("Provided password does not conform to standard format")
        if (!usernameFormat.matches(userRequest.userName))
            throw it.mag.wrongtzap.controller.web.exception.user.InvalidUsernameFormatException("Provided username does not conform to standard format")
        if (!emailFormat.matches(userRequest.userMail))
            throw it.mag.wrongtzap.controller.web.exception.user.InvalidEmailFormatException("Provided email is invalid")

        //check for already existing user
        if(userService.retrieveByEmail(userRequest.userMail) != null)
            throw it.mag.wrongtzap.controller.web.exception.user.UserAlreadyExistsException("User already exist")

        val user = User(
            username = userRequest.userName,
            email = userRequest.userMail.lowercase(),
            password = passwordEncoder.encode(userRequest.userPassword)
        )

        return userService.saveUser(user)
    }

    @Transactional
    fun createChat(chatRequest: ChatRequest): Chat {

        if(!chatNameFormat.matches(chatRequest.chatName))
            throw it.mag.wrongtzap.controller.web.exception.chat.InvalidChatnameFormatException()

        if(!chatRequest.isGroup && chatRequest.chatUsersIds.size != 2)
            throw it.mag.wrongtzap.controller.web.exception.chat.InvalidNumberOfParticipantsException()

        val participants: MutableSet<User> = mutableSetOf()
        chatRequest.chatUsersIds.forEach{ id ->
            participants.add(
                userService.retrieveById(id)
            )
        }

        val joinDates = participants.map { user -> user.userId  }.associateWith { LocalDateTime.now() }.toMutableMap()

        val newChat = Chat(
            name = chatRequest.chatName,
            participants = participants,
            userJoinDates = joinDates,
            isGroup = chatRequest.isGroup
        )

        return chatService.saveChat(newChat)

    }


    @Transactional
    fun addUserToChat(chatId: String, userId: String): Chat{
        val chat = chatService.retrieveChatById(chatId)
            ?: throw ChatNotFoundException()

        val user = userService.retrieveById(userId)


        chat.apply {
            userJoinDates[userId] = LocalDateTime.now()
            participants.add(user)
        }

        return chatService.saveChat(chat)
    }


    @Transactional
    fun createMessage(chatId: String, request: MessageRequest): Message{

        val sender = userService.retrieveById(request.userId)
        val chat = chatService.retrieveChatById(chatId)

        val message = Message(
            sender = sender,
            content = request.messageBody,
            associatedChat = chat,
        )

        return messageService.saveMessage(message)
    }


    @Transactional
    fun deleteMessageForSelf(chatId: String, messageId: String){

        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as Jwt
        val userEmail = jwt.subject

        val chat = chatService.retrieveChatById(chatId)
            ?: throw (ChatNotFoundException())

        val message = chat.messages.firstOrNull{ it.messageId == messageId}
            ?: throw (it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException())

        val userId = chat.participants.first{ user -> user.email == userEmail }.userId

        message.apply {
            deletedForUser.add(userId)
        }

        messageService.saveMessage(message)
    }

    @Transactional
    fun deleteMessageForEveryone(chatId: String, messageId: String){
        val chat = chatService.retrieveChatById(chatId)
            ?: throw (ChatNotFoundException())

        val message = chat.messages.firstOrNull{ it.messageId == messageId}
            ?: throw (it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException())

        message.deleteForEveryone()

        messageService.saveMessage(message)
    }


    fun login(userCredentials: LoginRequest): String{

        val user =  userService.retrieveByEmail(userCredentials.userMail)
            ?: throw it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException("User does not exist")

        val authenticated = passwordMatch(userCredentials.userPassword, user.password)

        if(authenticated){
            EmailCoroutineScope.launch {
                emailService.sendLoginNotification(user.email,user.userId)
            }
            return jwtUtil.generateToken(user.email)
        }
        else{
            throw it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInAuthentication("Email and/or Password are incorrect")
        }
    }

    fun changePassword(userId: String, newPasswordRequest: NewPasswordRequest): ResponseEntity<Any>{
        val user = userService.retrieveById(userId)

        val auth = passwordMatch(newPasswordRequest.oldPassword, user.password)

        if(auth){
            user.apply {
                user.password = passwordEncoder.encode(newPasswordRequest.newPassword)
                userService.saveUser(user)
                return ResponseEntity.status(200).body("Password Changed successfully")
            }
        }
        else throw it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInAuthentication("")
    }

    private fun passwordMatch(rawPassword: String, hashPassword: String): Boolean{
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.matches(rawPassword, hashPassword)
    }

}
