package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.model.base.BaseChat
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
        name = "groupMembers",
        joinColumns = [JoinColumn(name = "chat_id")],
        inverseJoinColumns = [JoinColumn(name = "participant_id")]
    )
    @JsonView(ViewsConfig.Public::class)
    @JsonBackReference("User-Groups")
    var members: MutableSet<User>,

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
        name = "user_join_entry",
        joinColumns = [JoinColumn(name = "chat_id")]
    )
    @MapKeyJoinColumn(name = "user_id")
    @Column(name = "join_date",)
    val userJoinEntry: MutableMap<String, Long>,

    @ElementCollection
    val archived: MutableList<String> = mutableListOf()

    ): BaseChat(
    creationDate = System.currentTimeMillis()
)