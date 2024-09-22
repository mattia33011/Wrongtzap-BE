package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.util.TimeGenUtil
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
data class Message(

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var messageId: String = "",

    @Column(updatable = false)
    @JsonView(ViewsConfig.Public::class)
    var timestamp: LocalDateTime = LocalDateTime.now(),

    @JsonView(ViewsConfig.Public::class)
    var content: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(ViewsConfig.Public::class)
    @JsonBackReference("Message-Sender")
    val sender: User,

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonView(ViewsConfig.Public::class)
    @JsonBackReference("Message-Chat")
    val associatedChat: Chat,

    @ElementCollection
    val deletedForUser: MutableSet<String> = mutableSetOf(),

    @Column(nullable = false)
    @JsonView(ViewsConfig.Internal::class)
    var deletedForEveryone: Boolean = false

){

    @PrePersist
    fun messageInit(){

        val userName = sender.userId.substringBefore("-")
        val preciseTime = TimeGenUtil.millisecondsFormat()

        messageId = "$userName-$preciseTime"
    }

    fun deleteForEveryone(){
        deletedForEveryone = true
    }

}

