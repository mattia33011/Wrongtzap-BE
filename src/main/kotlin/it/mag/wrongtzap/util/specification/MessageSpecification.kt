package it.mag.wrongtzap.util.specification

import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.GroupChat_
import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.Message_
import jakarta.persistence.criteria.Join
import org.springframework.data.jpa.domain.Specification

object MessageSpecification {



    fun notDeletedForEveryone(): Specification<Message>{
        return Specification { root, _, builder ->
            builder.not(root.get(Message_.deletedForEveryone))
        }
    }

    fun userCanSeeMessage(userId: String, groupId: String): Specification<Message>{

        return Specification { root, _, builder ->

            val groupJoin = root.join(Message_.associatedChat) as Join<Message, GroupChat>
            val entryJoin = groupJoin.join(GroupChat_.userJoinEntry)
            val deletedForUser = root.get(Message_.deletedForUser)

            entryJoin.get<String>(userId)

            val userJoinDateExpr = builder.selectCase<Long>()
                .`when`(builder.equal(entryJoin.key(),userId), entryJoin.value().`as`(Long::class.java))
                .otherwise(builder.literal(Long.MAX_VALUE))
            
            builder.and(
                builder.equal(groupJoin.get(GroupChat_.chatId), groupId),
                builder.greaterThan(root.get(Message_.timestamp), userJoinDateExpr),
                builder.or(
                    builder.isEmpty(deletedForUser),
                    builder.isNotMember(userId, deletedForUser)
                )
            )
        }
    }
}