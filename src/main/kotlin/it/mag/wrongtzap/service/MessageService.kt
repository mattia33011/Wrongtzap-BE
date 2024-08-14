package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.repository.ChatRepository
import it.mag.wrongtzap.repository.MessageRepository
import it.mag.wrongtzap.repository.UserRepository
import it.mag.wrongtzap.request.MessageRequest
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageService @Autowired constructor(
    private val messageRepository: MessageRepository,
)
{
    //Create method
    fun createMessage(message: Message) = messageRepository.save(message)


    //Retrieve method
    fun retrieveByKeyword(keyword: String) = messageRepository.findByMessageBody(keyword)
    fun retrieveById(id: Long) = messageRepository.findById(id)


    //Update method
    @Transactional
    fun updateMessage(id: Long, newBody: String): Message{
        val message = messageRepository.findById(id).orElseThrow{NullPointerException("Message not found")}

        message.apply {
            messageBody = newBody
        }

        return messageRepository.save(message)

    }

    //Delete method
    @Transactional
    fun deleteMessage(id :Long): Message {
        val message = messageRepository.findById(id).orElseThrow{NullPointerException("Message not found")}

        message.apply{
            messageBody = "This message has been deleted"
        }

        return messageRepository.save(message)
    }


}