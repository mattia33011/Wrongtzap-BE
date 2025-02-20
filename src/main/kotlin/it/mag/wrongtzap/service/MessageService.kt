package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.page.request.PageMessageRequest
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.repository.MessageRepository
import it.mag.wrongtzap.util.specification.MessageSpecification
import org.springframework.data.jpa.domain.Specification

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class MessageService @Autowired constructor(
    private val messageRepository: MessageRepository,
)
{
    //Create method
    fun saveMessage(message: Message) = messageRepository.save(message)


    //Retrieve method
    fun retrieveByKeyword(keyword: String) = messageRepository.findByContent(keyword)
    fun retrieveById(messageId: String) = messageRepository.findById(messageId)
    fun retrieveAll() = messageRepository.findAll()

    fun returnPage(userId: String, chatId: String): Page<Message> {

        val spec = Specification.where(
            MessageSpecification.notDeletedForEveryone()
                .and(MessageSpecification.userCanSeeMessage(userId, chatId))
        )

        val page= PageRequest.of(0, 15)

        return messageRepository.findAll(spec, page)
    }

    fun returnPage(request: PageMessageRequest): Page<Message> {

        val spec = Specification.where(
            MessageSpecification.notDeletedForEveryone()
                .and(MessageSpecification.userCanSeeMessage(request.userId, request.chatId))
        )

        val page = PageRequest.of(request.pageNumber.toInt(), request.pageSize.toInt())
        return messageRepository.findAll(spec, page)
    }

    fun returnPage(pageNumber: Int, pageSize: Int, userId: String, chatId: String): Page<Message> {

        val spec = Specification.where(
            MessageSpecification.notDeletedForEveryone()
                .and(MessageSpecification.userCanSeeMessage(userId, chatId))
        )

        val page= PageRequest.of(pageNumber, pageSize)

        return messageRepository.findAll(spec, page)
    }

    fun retrieveChatFilteredPage(userId: String, chatId: String): Page<Message> {

        val spec = Specification.where(
            MessageSpecification.notDeletedForEveryone()
        )

        val page= PageRequest.of(0, 15)

        return messageRepository.findAll(spec, page)
    }

    fun retrieveChatFilteredPage(pageNumber: Int, pageSize: Int, userId: String, chatId: String): Page<Message> {

        val spec = Specification.where(
            MessageSpecification.notDeletedForEveryone()
        )

        val page= PageRequest.of(pageNumber, pageSize)

        return messageRepository.findAll(spec, page)
    }

    //Update method
    @Transactional
    fun editMessage(messageId: String, newBody: String): Message{
        val message = messageRepository.findById(messageId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException() }

        message.apply {
            content = newBody
        }

        return messageRepository.save(message)

    }


    //Delete method
    @Transactional
    fun deleteMessage(messageId: String): Message {
        val message = messageRepository.findById(messageId)
            .orElseThrow{ it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException() }

        message.apply{
            content = "This message has been deleted"
        }

        return messageRepository.save(message)
    }


}