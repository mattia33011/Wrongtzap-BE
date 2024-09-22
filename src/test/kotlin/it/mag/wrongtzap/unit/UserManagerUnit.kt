package it.mag.wrongtzap.unit

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import it.mag.wrongtzap.exception.chat.InvalidChatnameFormatException
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.request.ChatRequest
import it.mag.wrongtzap.request.LoginRequest
import it.mag.wrongtzap.request.MessageRequest
import it.mag.wrongtzap.service.ChatService
import it.mag.wrongtzap.service.EmailService
import it.mag.wrongtzap.service.MessageService
import it.mag.wrongtzap.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals

@SpringBootTest
class UserManagerUnit {

    val messageService: MessageService = mockk()
    val userService: UserService = mockk()
    val chatService: ChatService = mockk()
    val emailService: EmailService = mockk()
    val jwtUtil: JwtUtil = mockk()
    val passwordEncoder: PasswordEncoder = mockk()

    lateinit var userManager: UserManager


    @BeforeEach
    fun runBefore(){

        userManager = spyk(
            UserManager(
                messageService,
                userService,
                chatService,
                emailService,
                passwordEncoder,
                jwtUtil),
            recordPrivateCalls = true
        )
    }


    @ParameterizedTest
    @CsvSource(
        //Format email, password, exception
        "totallynotafakeemail@gmail.com, thisIsAStrongPass, ''",
        "fakeemail@gmail.it, thisIsAStrongPass, 'UserNotFoundException'",
        "totallynotafakeemail@gmail.eu, thisIsAStrongPass, 'UserNotFoundException'",
        "totallynotafakeemail@gmail.com, justAPassword, 'UserNotFoundInAuthentication'",
        "totallynotafakeemail@gmail.com, somewrongPass, 'UserNotFoundInAuthentication'"

    )
    fun should_check_for_existing_credentials_or_throw_exception(email: String, password: String, exception: String){


        val loginRequest = LoginRequest(userPassword = password, userMail = email)

        val testUser = mockk<User>()

        val existingMail = "totallynotafakeemail@gmail.com"
        val existingPassword = "thisIsAStrongPass"

        every { testUser.userId  } returns "someid"
        every { testUser.email } returns  email
        every { testUser.password } returns "encoded"

        every { userService.retrieveByEmail(any()) } answers{
            if (email == existingMail) testUser else null
        }

        every { userManager["passwordMatch"](any<String>(), any<String>()) } answers {
            password == existingPassword
        }

        every { emailService.sendLoginNotification(any(), any()) } returns Unit
        every { jwtUtil.generateToken(any()) } returns "token"


        if(exception.isEmpty()){
            val token = userManager.login(loginRequest)

            assertEquals(token, "token")
            verify(exactly = 1){ emailService.sendLoginNotification(any(),any()) }
            verify(exactly = 1){ jwtUtil.generateToken(any()) }
        }
        else{
            val ex = assertThrows<Exception> {
                userManager.login(loginRequest)
            }
            assertEquals(exception, ex::class.simpleName)
        }

        verify(exactly = 1) { userService.retrieveByEmail(any()) }
    }

    @ParameterizedTest
    @CsvSource(
        "lorem ipsum, true",
        "finding treasure together, true",
        "65454242&&&&$$%, false",
        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa, false"
    )
    fun should_create_chat_or_throw_exceptions(chatName: String, valid: Boolean){

        val testUser1 = mockk<User>()
        val testUser2 = mockk<User>()

        val testUserList = mutableSetOf(testUser1,testUser2)

        val userId1 = "firstId"
        val userId2 = "secondId"

        val chatRequest = ChatRequest(
            chatName = chatName,
            chatUsersIds = listOf(userId1,userId2)
        )

        every { userService.retrieveById("firstId") } returns testUser1
        every { userService.retrieveById("secondId") } returns testUser2

        every { chatService.saveChat(any()) } answers {firstArg()}

        if (valid){
            val generatedChat = userManager.createChat(chatRequest)
            assertEquals(generatedChat.name,chatName)
            assert(generatedChat.participants.containsAll(testUserList))
            verify(exactly = 2) { userService.retrieveById(any()) }
            verify(exactly = 1) { chatService.saveChat(any()) }
        }
        else{
            assertThrows<InvalidChatnameFormatException> {
                userManager.createChat(chatRequest)
            }
        }
    }

    @ParameterizedTest
    @CsvSource(
        //Format user_id, chat_id, message_content
        "vincenzo, somechat78, questa e benzina",
        "novamaster, bloody rabbits, la puffaaaaaaaa",
        "somedude, solo destiny content, soloing the architects"
    )
    fun should_create_message_or_trow_exception(userId: String, chatId: String, messageBody: String){

        val testUser = mockk<User>()
        val testChat = mockk<Chat>()
        val testRequest = MessageRequest(userId, messageBody)

        every { testUser.userId } returns userId
        every { testChat.chatId } returns chatId

        every {userService.retrieveById(userId) } returns testUser
        every {chatService.retrieveChatById(chatId)} returns testChat

        every { messageService.saveMessage(any()) } answers {firstArg()}

        val returnedMessage = userManager.createMessage(chatId, testRequest)

        verify (exactly = 1){ userService.retrieveById(userId)}
        verify (exactly = 1){ chatService.retrieveChatById(chatId)}
        verify (exactly = 1){ messageService.saveMessage(any()) }

        assertEquals(returnedMessage.sender.userId, userId)
        assertEquals(returnedMessage.content,messageBody)
        assertEquals(returnedMessage.associatedChat.chatId,chatId)
    }
}