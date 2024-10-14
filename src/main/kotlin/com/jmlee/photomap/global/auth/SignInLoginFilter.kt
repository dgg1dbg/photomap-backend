package com.jmlee.photomap.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.jmlee.photomap.domain.user.dto.UserDto
import com.jmlee.photomap.global.auth.dto.LoginRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.BufferedReader


class SignInLoginFilter (
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    ) : UsernamePasswordAuthenticationFilter() {
    init {
        setFilterProcessesUrl("/api/user/signin")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val parsedRequest: LoginRequest = parseRequest(request)
        val email = parsedRequest.email
        val password = parsedRequest.password
        val authRequest = UsernamePasswordAuthenticationToken(email, password)
        return authenticationManager.authenticate(authRequest)
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        response.addHeader("Authentication", jwtTokenProvider.generateToken(authResult))
        response.status = HttpServletResponse.SC_OK
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        super.unsuccessfulAuthentication(request, response, failed);
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }

    private fun parseRequest(request: HttpServletRequest): LoginRequest {
        val reader: BufferedReader = request.reader
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(reader, LoginRequest::class.java)
    }
}