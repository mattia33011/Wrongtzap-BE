package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: JpaRepository<Message,Long> {
    fun findByContent(body: String): List<Message>

}