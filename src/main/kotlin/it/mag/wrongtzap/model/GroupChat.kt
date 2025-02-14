package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.base.Chat
import it.mag.wrongtzap.util.IdGenUtil
import jakarta.persistence.*

@Entity
@Table(indexes = [
    Index(name = "idx_name", columnList = "chat_name")
])
data class GroupChat (

    @Column(nullable = false, name = "chat_name")
    var name: String,

    @ManyToMany
    @JoinTable(
        name = "groupParticipants",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "participant_id")]
    )
    @JsonView(ViewsConfig.Public::class)
    @JsonBackReference("User-Groups")
    var participants: MutableSet<User>,

    @ManyToMany
    @JoinTable(
        name = "groupAdministrators",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "administrator_id")]
    )
    @JsonBackReference
    var admins: MutableSet<User>,


    @ElementCollection
    @CollectionTable(
        name = "user_join_date",
        joinColumns = [JoinColumn(name = "chat_id")]
    )
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "join_date",)
    val userJoinDates: MutableMap<Long, Long>,

    @ElementCollection
    val archived: MutableList<String> = mutableListOf()

    ): Chat(
    creationDate = System.currentTimeMillis()
)