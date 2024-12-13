package it.mag.wrongtzap.model.base

import com.fasterxml.jackson.annotation.JsonManagedReference
import it.mag.wrongtzap.model.Message
import jakarta.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class Chat(
    @Id
    @Column(updatable = false, nullable = false)
    open val chatId: String,

    @Column(updatable = false)
    open val creationDate: Long,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "associatedChat")
    @JsonManagedReference("Chat-Messages")
    open var messages: MutableList<Message> = mutableListOf(),
)



