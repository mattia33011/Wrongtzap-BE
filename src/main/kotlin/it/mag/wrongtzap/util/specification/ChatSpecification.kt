package it.mag.wrongtzap.util.specification

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.Chat_
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.GroupChat_
import it.mag.wrongtzap.model.User_
import org.springframework.data.jpa.domain.Specification

object ChatSpecification {
    fun containsUser(userId: String): Specification<Chat> {
        return Specification { root, query, builder ->
            val chatUsers = root.join(Chat_.members)
            query.multiselect(
                root.get(Chat_.chatId),
                root.get(Chat_.members),
                root.get(Chat_.creationDate),
                root.get(Chat_.archived),
            )
            builder.equal(chatUsers.get(User_.userId), userId)
        }
    }
}