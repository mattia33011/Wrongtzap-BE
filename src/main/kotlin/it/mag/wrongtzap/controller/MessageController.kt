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
    @PostMapping
    @JsonView(ViewsConfig.Public::class)
    fun postMessage(@RequestBody message: Message) = messageService.createMessage(message)

    @DeleteMapping("/{messageId}")
    @JsonView(ViewsConfig.Public::class)
    fun deleteMessage(@PathVariable messageId: String) = messageService.deleteMessage(messageId)
}