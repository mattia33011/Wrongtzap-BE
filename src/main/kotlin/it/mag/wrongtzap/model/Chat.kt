package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.util.IdGenUtil
import jakarta.persistence.*


@Entity
data class Chat (

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var chatId: String = "",

    @JsonView(ViewsConfig.Public::class)
    var chatName: String,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)

    @JoinTable(
        name = "chatParticipants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "participants_id")]
    )
    @JsonView(ViewsConfig.Public::class)
    var chatParticipants: MutableSet<User> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JsonView(ViewsConfig.Internal::class)
    var chatMessages: MutableList<Message> = mutableListOf(),
    ) {

    @PrePersist
    fun chatInit() {
        if (chatId.isEmpty()) {
            chatId = IdGenUtil.generateChatId(chatName)
        }
    }
}

