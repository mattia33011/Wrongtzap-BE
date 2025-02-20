package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import jakarta.persistence.*


@Entity
@Table(indexes = [
    Index(name = "idx_password", columnList = "user_password"),
    Index(name = "idx_email", columnList = "user_email")
])
data class User(

    @Id
    @Column(updatable = false, nullable = false, name = "user_id")
    @JsonView(ViewsConfig.Public::class)
    val userId: String,

    @JsonView(ViewsConfig.Public::class)
    @Column(nullable = false, name = "user_name")
    var username: String,

    @JsonView(ViewsConfig.Public::class)
    @Column(nullable = false, name = "user_email")
    var email: String,

    @JsonView(ViewsConfig.Internal::class)
    @Column(nullable = false, name = "user_password")
    var password: String,
){

    @ManyToMany(mappedBy = "members", cascade = [CascadeType.ALL])
    @JsonManagedReference("User-Chats")
    var chats: MutableSet<Chat> = mutableSetOf()

    @ManyToMany(mappedBy = "members", cascade = [CascadeType.ALL])
    @JsonManagedReference("User-Groups")
    var groups: MutableSet<GroupChat> = mutableSetOf()

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.ALL], orphanRemoval = true)
    val outboundFriendships: MutableSet<Friend> = mutableSetOf()

    @OneToMany(mappedBy = "receiver", cascade = [CascadeType.ALL], orphanRemoval = true)
    val inboundFriendships: MutableSet<Friend> = mutableSetOf()
}