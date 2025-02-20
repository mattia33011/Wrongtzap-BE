package it.mag.wrongtzap.util.specification

import arrow.core.prependTo
import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.GroupChat_
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.User_
import jakarta.persistence.criteria.Selection
import org.springframework.data.jpa.domain.Specification

object GroupSpecification {
    fun containsUser(userId: String): Specification<GroupChat> {
        return Specification { root, query, builder ->
            val groupUsers = root.join(GroupChat_.members)
            query.multiselect(
                root.get(GroupChat_.chatId),
                root.get(GroupChat_.members),
                root.get(GroupChat_.creationDate),
                root.get(GroupChat_.name),
                root.get(GroupChat_.archived),
                root.get(GroupChat_.admins),
                root.get(GroupChat_.userJoinEntry),
            )
            builder.equal(groupUsers.get(User_.userId), userId)

        }
    }
}