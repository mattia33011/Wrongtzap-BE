package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.GroupChat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupChatRepository: JpaRepository<GroupChat, String> {
}