package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.web.exception.message.MessageNotFoundException
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.repository.MessageRepository

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
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