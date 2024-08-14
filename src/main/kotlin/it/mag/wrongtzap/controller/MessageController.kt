package it.mag.wrongtzap.controller


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
    fun postMessage(@RequestBody message: Message) = messageService.createMessage(message)

    @GetMapping("/{messageId}")
    fun getMessage(@PathVariable messageId: Long) = messageService.retrieveById(messageId)

    @PatchMapping("/{messageId}")
    fun patchMessage(@PathVariable messageId: Long, @RequestBody newBody: String) = messageService.updateMessage(messageId, newBody)

    @DeleteMapping("/{messageId}")
    fun deleteMessage(@PathVariable messageId: Long) = messageService.deleteMessage(messageId)
}