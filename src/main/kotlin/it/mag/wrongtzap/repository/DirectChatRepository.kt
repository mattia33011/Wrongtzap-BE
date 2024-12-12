package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.DirectChat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DirectChatRepository: JpaRepository<DirectChat, String>
{
    override fun deleteById(chatId: String)
}