package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.util.IdGenUtil
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
data class Chat (

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var chatId: String = "",

    @JsonView(ViewsConfig.Public::class)
    var name: String,


    @ManyToMany
    @JoinTable(
        name = "chatParticipants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "participants_id")]
    )
    @JsonView(ViewsConfig.Public::class)
    @JsonManagedReference("Chat-Users")
    var participants: MutableSet<User>,


    @ElementCollection
    @CollectionTable(
        name = "user_join_date",
        joinColumns = [JoinColumn(name = "chat_id")]
    )
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "join_date",)
    @JsonView(ViewsConfig.Public::class)
    val userJoinDates: MutableMap<String, Long>,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "associatedChat")
    @JsonView(ViewsConfig.Internal::class)
    @JsonManagedReference("Chat-Messages")
    var messages: MutableList<Message> = mutableListOf(),

    val isGroup: Boolean,
){

    @PrePersist
    fun chatInit() {
        if (chatId.isEmpty()) {
            chatId = IdGenUtil.generateChatId(name)
        }
    }
}

