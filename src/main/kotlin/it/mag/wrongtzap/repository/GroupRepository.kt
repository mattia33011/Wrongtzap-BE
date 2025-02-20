package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.GroupChat
import it.mag.wrongtzap.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
@Repository
interface GroupRepository: JpaRepository<GroupChat, String>, JpaSpecificationExecutor<GroupChat> {
    fun findByMembersContains(user: User, page: Pageable): Page<GroupChat>
}