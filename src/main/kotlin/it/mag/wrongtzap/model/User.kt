package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse
import it.mag.wrongtzap.util.IdGenUtil
import jakarta.persistence.*


@Entity
data class User(

    @Id
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    var userId: String = "",

    @JsonView(ViewsConfig.Public::class)
    var username: String,

    @JsonView(ViewsConfig.Public::class)
    var email: String,

    @JsonView(ViewsConfig.Internal::class)
    var password: String,

    @ElementCollection(fetch = FetchType.EAGER)
    val friends: MutableSet<ProfileResponse> = mutableSetOf()


){

    @PrePersist
    fun userInit() {
        if (userId.isEmpty()) {
            userId = IdGenUtil.generateUserId(username)
        }
    }

    @ManyToMany(mappedBy = "participants", cascade = [CascadeType.ALL])
    @JsonManagedReference("User-Chats")
    var directChats: MutableSet<DirectChat> = mutableSetOf()

    @ManyToMany(mappedBy = "participants", cascade = [CascadeType.ALL])
    @JsonManagedReference("User-Groups")
    var groupChats: MutableSet<GroupChat> = mutableSetOf()

}