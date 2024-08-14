package it.mag.wrongtzap.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import it.mag.wrongtzap.config.ViewsConfig
import jakarta.persistence.*

import org.hibernate.annotations.UuidGenerator


/*
    Qui ho cambiato un po' di cose:

        1) ho cambiato la class in data class perche' questa classe qui sara' adibita solo ed esclusivamente come dato
        2) ho creato un secondo costruttore senza uid, perche' cosi' per istanziare User non dobbiamo per forza inizializzare uid
           dato che viene autogenerato
        3) ho spostato uid nel costruttore princale e l'ho tolto dal corpo della classe (per intenderci dentro le graffe)
        4) ho eliminato i private (Era per questo motivo che ti dava tutto null perche' il mapper non riusciva ad accedere agli attributi private)
 */

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "userId")
data class User(
    @JsonView(ViewsConfig.Public::class)
    var userName: String,
    @JsonView(ViewsConfig.Public::class)
    var userMail: String,
){
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    @JsonView(ViewsConfig.Public::class)
    lateinit var userId: String

    @ManyToMany(mappedBy = "chatParticipants", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonView(ViewsConfig.Internal::class)
    var userChats: MutableSet<Chat> = mutableSetOf()
}