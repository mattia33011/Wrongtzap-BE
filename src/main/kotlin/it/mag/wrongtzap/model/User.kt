package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
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
    var username: String,

    @JsonView(ViewsConfig.Public::class)
    var email: String,

    @JsonView(ViewsConfig.Internal::class)
    var password: String,
){

    @Id
    @PrePersist
    fun userInit() {
        if (userId.isEmpty()) {
            userId = IdGenUtil.generateUserId(username)
        }
    }

    @ManyToMany(mappedBy = "participants", cascade = [CascadeType.ALL])
    @JsonView(ViewsConfig.Internal::class)
    @JsonBackReference("User-Chats")
    var chats: MutableSet<Chat> = mutableSetOf()
}