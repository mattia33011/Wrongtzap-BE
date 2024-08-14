package it.mag.wrongtzap.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Message(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val messageId: Long? = null,

    @Column(updatable = false)
    var messageTime: String? = null,

    @ManyToOne
    val messageSender: User,

    var messageBody: String
){
    @PrePersist
    fun prePersist(){
        messageTime = LocalDateTime.now().toString()
    }
}
