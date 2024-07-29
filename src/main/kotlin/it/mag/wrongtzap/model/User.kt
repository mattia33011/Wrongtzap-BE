package it.mag.wrongtzap.model

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


/*
    Qui ho cambiato un po' di cose:

        1) ho cambiato la class in data class perche' questa classe qui sara' adibita solo ed esclusivamente come dato
        2) ho creato un secondo costruttore senza uid, perche' cosi' per istanziare User non dobbiamo per forza inizializzare uid
           dato che viene autogenerato
        3) ho spostato uid nel costruttore princale e l'ho tolto dal corpo della classe (per intenderci dentro le graffe)
        4) ho eliminato i private (Era per questo motivo che ti dava tutto null perche' il mapper non riusciva ad accedere agli attributi private)
 */

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val uid: Long?,
    val username: String,
    val email: String
) {
    constructor(username: String, email: String) : this(null, username, email)
    fun mappingToString() = "ciao"
}