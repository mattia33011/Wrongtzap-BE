package it.mag.wrongtzap.model.base

import com.fasterxml.jackson.annotation.JsonManagedReference
import it.mag.wrongtzap.model.Message
import jakarta.persistence.*


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class BaseChat(
    @Id
    @Column(updatable = false, nullable = false)
    open var chatId: String = "",

    @Column(updatable = false)
    open val creationDate: Long,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "associatedChat")
    @JsonManagedReference("Chat-Messages")
    @OrderBy("timestamp ASC")
    open var messages: MutableList<Message> = mutableListOf(),
)



