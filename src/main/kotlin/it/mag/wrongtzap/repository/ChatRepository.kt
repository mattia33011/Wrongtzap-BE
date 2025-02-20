package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.Chat
import it.mag.wrongtzap.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<Chat, String>, JpaSpecificationExecutor<Chat> {
    override fun deleteById(chatId: String)
    fun findByMembersContains(user: User, page: Pageable): Page<Chat>
}