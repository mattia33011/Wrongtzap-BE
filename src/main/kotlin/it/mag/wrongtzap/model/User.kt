package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.util.IdGenUtil
import jakarta.persistence.*


@Entity
data class User(

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var userId: String = "",

    @JsonView(ViewsConfig.Public::class)
    var userName: String,

    @JsonView(ViewsConfig.Public::class)
    var userMail: String,

    @JsonView(ViewsConfig.Public::class)
    val userPassword: String,
){

    @Id
    @PrePersist
    fun userInit() {
        if (userId.isEmpty()) {
            userId = IdGenUtil.generateUserId(userName)
        }
    }

    @ManyToMany(mappedBy = "chatParticipants", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonView(ViewsConfig.Internal::class)
    var userChats: MutableSet<Chat> = mutableSetOf()

    @OneToMany(mappedBy = "messageSender")
    @JsonView(ViewsConfig.Internal::class)
    val userMessages: MutableSet<Message> = mutableSetOf()
}