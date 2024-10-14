package com.jmlee.photomap.global.auth.model

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CustomAuthenticationToken(
    private val principal: UserPrincipal,
    private var accessToken: Any?,
    authorities: Collection<GrantedAuthority?>?,
) : AbstractAuthenticationToken(authorities) {
    init {
        if (authorities == null) super.setAuthenticated(false)
        else super.setAuthenticated(true)
    }
    override fun getPrincipal(): UserPrincipal = principal
    override fun getCredentials(): Any? = accessToken
    override fun eraseCredentials(){
        super.eraseCredentials()
        accessToken = null
    }
}