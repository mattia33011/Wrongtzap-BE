package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.Profile
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository: JpaRepository<Profile , Long> {

}