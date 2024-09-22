package it.mag.wrongtzap.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserDetail(

    private val username: String,
    private val email : String,
    private val password: String,
    private val enabled: Boolean,
    private val roles: List<String>

) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it) }
    }

    override fun getPassword(): String = password

    fun getEmail(): String = email

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}