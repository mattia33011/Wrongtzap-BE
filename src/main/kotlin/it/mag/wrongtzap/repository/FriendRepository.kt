package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.Friend
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FriendRepository: JpaRepository<Friend, String>{

}