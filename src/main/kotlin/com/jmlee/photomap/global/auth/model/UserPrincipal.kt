package com.jmlee.photomap.global.auth.model

import com.jmlee.photomap.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserPrincipal(val user: User) : UserDetails {
    override fun getUsername(): String = user.email
    override fun getPassword(): String = user.password
    override fun getAuthorities(): List<GrantedAuthority>{
        val roles: List<String> = user.roles.split(",").filter {it.isNotEmpty()}
        return roles.map{SimpleGrantedAuthority(it)}
    }
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}