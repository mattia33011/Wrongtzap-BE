package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.base.BaseChat
import jakarta.persistence.*


@Entity
data class Message(

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var messageId: String,

    @Column(updatable = false)
    @JsonView(ViewsConfig.Public::class)
    var timestamp: Long,

    @JsonView(ViewsConfig.Public::class)
    var content: String,

    @ManyToOne
    @JoinColumn( nullable = false)
    @JsonView(ViewsConfig.Public::class)
    @JsonBackReference("Message-Sender")
    val sender: User,

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonBackReference("Message-Chat")
    val associatedChat: BaseChat,

    @ElementCollection
    val deletedForUser: MutableSet<String> = mutableSetOf(),

    @Column(nullable = false)
    @JsonView(ViewsConfig.Internal::class)
    var deletedForEveryone: Boolean = false

){

    fun deleteForEveryone(){
        deletedForEveryone = true
    }
}

