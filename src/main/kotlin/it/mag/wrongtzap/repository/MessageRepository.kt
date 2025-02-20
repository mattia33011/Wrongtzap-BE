package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.Message
import it.mag.wrongtzap.model.base.BaseChat
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository


@Repository
interface MessageRepository: JpaRepository<Message,String>, JpaSpecificationExecutor<Message> {
    fun findByContent(body: String): List<Message>
    fun findByAssociatedChat(chat: BaseChat, page: Pageable): Page<Message>
}