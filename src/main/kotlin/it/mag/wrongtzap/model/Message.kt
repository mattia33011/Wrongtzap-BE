package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.util.TimeGenUtil
import jakarta.persistence.*


@Entity
data class Message(

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var messageId: String = "",

    @Column(updatable = false)
    @JsonView(ViewsConfig.Public::class)
    var messageTime: String = "",

    @JsonView(ViewsConfig.Public::class)
    var messageBody: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(ViewsConfig.Public::class)
    val messageSender: User,

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonView(ViewsConfig.Internal::class)
    val messageChat: Chat
){


    @PrePersist
    fun messageInit(){

        val userName = messageSender.userId.substringBefore("-")
        val preciseTime = TimeGenUtil.millisecondsFormat()

        messageTime = TimeGenUtil.minutesFormat()
        messageId = "$userName-$preciseTime"

    }

}

