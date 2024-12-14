package it.mag.wrongtzap.model

import it.mag.wrongtzap.model.base.Chat
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
data class DirectChat(

    @ManyToMany
    @JoinTable(
        name = "chatParticipants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @Size(min = 2, max = 2)
    var participants: List<User>,

    @ElementCollection
    @Size(min = 0, max = 2)
    var archived: List<String> = listOf()

): Chat(
    chatId = participants[0].userId + participants[1].userId,
    creationDate = System.currentTimeMillis()
)