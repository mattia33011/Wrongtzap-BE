package it.mag.wrongtzap.model

import it.mag.wrongtzap.util.enums.FriendStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
data class Friend (

    @Id
    @Column(updatable = false, nullable = false)
    val requestId: String,

    @ManyToOne
    @JoinColumn(name = "sender_id")
    val sender: User,

    @ManyToOne
    @JoinColumn(name ="receiver_id")
    val receiver: User,

    @Column(nullable = false)
    var status: FriendStatus
)

