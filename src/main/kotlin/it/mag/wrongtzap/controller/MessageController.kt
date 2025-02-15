package it.mag.wrongtzap.controller


import it.mag.wrongtzap.service.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/messages")
class MessageController @Autowired constructor(
    private val  messageService: MessageService
)
{
    @DeleteMapping("/{messageId}")
    fun deleteMessage(@PathVariable messageId: String) = messageService.deleteMessage(messageId)
}