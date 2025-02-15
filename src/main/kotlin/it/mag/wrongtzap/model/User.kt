package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse
import it.mag.wrongtzap.util.IdGenUtil
import jakarta.persistence.*
import okhttp3.internal.userAgent
import org.springframework.context.annotation.Primary


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

    @ManyToMany(mappedBy = "participants", cascade = [CascadeType.ALL])
    @JsonManagedReference("User-Chats")
    var directChats: MutableSet<DirectChat> = mutableSetOf()

    @ManyToMany(mappedBy = "participants", cascade = [CascadeType.ALL])
    @JsonManagedReference("User-Groups")
    var groupChats: MutableSet<GroupChat> = mutableSetOf()

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_friends",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Column(name = "friend_id")
    val friends: MutableList<String> = mutableListOf()

}