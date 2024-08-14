package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<Chat, String>
{
    override fun deleteById(chatId: String)
}