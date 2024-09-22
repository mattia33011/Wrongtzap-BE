package it.mag.wrongtzap.controller


import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.request.MessageRequest
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
    @JsonView(ViewsConfig.Public::class)
    fun deleteMessage(@PathVariable messageId: String) = messageService.deleteMessage(messageId)
}