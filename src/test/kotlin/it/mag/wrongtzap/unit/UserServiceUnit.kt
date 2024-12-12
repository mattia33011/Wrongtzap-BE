package it.mag.wrongtzap.unit

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import it.mag.wrongtzap.service.UserService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
class UserServiceUnit {
    val userRepository: UserRepository = mockk()

    lateinit var userService: UserService

    @BeforeEach
    fun runBefore(){
        userService = UserService(userRepository)
    }


    @ParameterizedTest
    @CsvSource(
        //Format: chat_id, exists
        "theonepiece, true",
        "404, false",
        "Molise, false"

    )
    fun should_retrieve_chat_or_throw_exception(someChatId: String, exists: Boolean) {

        val mockChat1= mockk<DirectChat>()
        val mockChat2= mockk<DirectChat>()
        val mockUser = mockk<User>()

        every { userRepository.findById(any()) } returns Optional.of(mockUser)
        every { mockUser.chats} answers {
            if (exists) mutableSetOf(mockChat1) else mutableSetOf(mockChat2)
        }

        every {mockChat1.chatId} returns someChatId
        every {mockChat2.chatId} returns "someNonsense"

        if (exists){
            val returnedChat = userService.retrieveChat("someId", someChatId)
            assertEquals(returnedChat, mockChat1)
        }
        else {
            assertThrows<it.mag.wrongtzap.controller.web.exception.chat.ChatNotFoundException> {
                userService.retrieveChat("someId", someChatId)
            }
        }

        verify { userRepository.findById(any()) }
        verify { mockUser.chats }

    }

    @ParameterizedTest
    @CsvSource(
        //Format: message, valid
        "this, true",
        "another, true",
        "with, true",
        "day, true",
        "we are so screwed, false",
        "pointer, false",
        "this is a message, true",
        "what is the value of m?, false"
    )
    fun should_find_some_messages_or_throw_exception(search: String, valid: Boolean){

        val mockMessage1 = mockk<Message>()
        val mockMessage2 = mockk<Message>()
        val mockMessage3 = mockk<Message>()

        val mockChat1 = mockk<DirectChat>()
        val mockChat2 = mockk<DirectChat>()

        val mockMessageList1 = mutableListOf(mockMessage1, mockMessage2)
        val mockMessageList2 = mutableListOf(mockMessage3)
        val mockChatList = mutableSetOf(mockChat1, mockChat2)

        val mockUser = mockk<User>()

        every { userRepository.findById(any())} returns Optional.of(mockUser)
        every { mockUser.chats } returns  mockChatList

        every { mockChat1.messages} returns  mockMessageList1
        every { mockChat2.messages} returns  mockMessageList2

        every { mockMessage1.content} returns  "this is a message"
        every { mockMessage2.content} returns  "another day another message"
        every { mockMessage3.content} returns  "nothing to do with the other two"

        if (valid){
            val messages = userService.searchMessages("someId", search)

            assert(messages.isNotEmpty())

            messages.forEach{ message ->
                assert(message.content.contains(search))
            }
        }
        else{
            assertThrows<it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException> {
                userService.searchMessages("someId", search)
            }
        }

        verify { userRepository.findById("someId") }
        verify { mockUser.chats }
        verify { mockChat1.messages }
        verify { mockChat2.messages }
    }



}

