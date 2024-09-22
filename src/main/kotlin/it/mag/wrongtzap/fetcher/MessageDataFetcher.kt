package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import graphql.schema.DataFetchingEnvironment
import it.mag.wrongtzap.exception.message.MessageNotFoundException
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.service.MessageService
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class MessageDataFetcher @Autowired constructor(
    val messageService: MessageService
) {

    @DgsQuery(field = "Message")
    fun getMessage(@InputArgument messageId: String) = messageService.retrieveById(messageId)

    @DgsQuery
    fun searchMessage(@InputArgument messageBody: String) = messageService.retrieveByKeyword(messageBody)

    @DgsQuery(field = "everyMessage")
    fun getEveryMessage() = messageService.retrieveAll()

    @DgsData(parentType = "Message", field = "sender")
    fun getUser(dfe: DataFetchingEnvironment): String {

        val message = dfe.getSource<Message>() ?: throw MessageNotFoundException()

        return message.sender.userId

    }

    @DgsData(parentType = "Message", field = "associatedChat")
    fun getChat(dfe: DataFetchingEnvironment): String{
        val message = dfe.getSource<Message>() ?: throw MessageNotFoundException()

        return message.associatedChat.chatId
    }





}
