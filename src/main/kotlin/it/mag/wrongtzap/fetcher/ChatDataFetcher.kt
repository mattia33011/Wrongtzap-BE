package it.mag.wrongtzap.fetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.repository.ChatRepository
import org.springframework.beans.factory.annotation.Autowired

@DgsComponent
class ChatDataFetcher@Autowired constructor(
    private val chatRepository: ChatRepository
) {
}