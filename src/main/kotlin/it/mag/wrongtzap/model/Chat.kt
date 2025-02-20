package it.mag.wrongtzap.model

import it.mag.wrongtzap.model.base.BaseChat
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
data class Chat(

    @ManyToMany
    @JoinTable(
        name = "chatMembers",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @Size(min = 2, max = 2)
    var members: List<User>,

    @ElementCollection
    @Size(min = 0, max = 2)
    var archived: List<String> = listOf()

): BaseChat(
    creationDate = System.currentTimeMillis()
)