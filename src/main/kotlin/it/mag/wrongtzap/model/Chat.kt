package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import it.mag.wrongtzap.config.ViewsConfig
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.beans.factory.annotation.Autowired


@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "chatId")
data class Chat (

    @JsonView(ViewsConfig.Public::class)
    var chatName: String,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)

    @JoinTable(
        name = "chatParticipants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "participants_id")]
    )
    @JsonView(ViewsConfig.Internal::class)
    var chatParticipants: MutableSet<User> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL])
    @JsonView(ViewsConfig.Internal::class)
    var chatMessages: MutableList<Message> = mutableListOf(),
    ) {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    lateinit var chatId: String
}