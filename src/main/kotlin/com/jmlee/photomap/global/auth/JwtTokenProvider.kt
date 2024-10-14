package com.jmlee.photomap.global.auth

import com.jmlee.photomap.domain.user.repository.UserRepository
import com.jmlee.photomap.global.auth.model.CustomAuthenticationToken
import com.jmlee.photomap.global.auth.model.UserPrincipal
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider (private val userRepository: UserRepository){
    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    val tokenPrefix = "Bearer "
    val headerString = "Authentication"
    private val jwtSecretKey = "jeongminlee"
    private val jwtExpirationInMs = 10800000

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserPrincipal
        return generateToken(userPrincipal.user.email)
    }

    fun generateToken(userEmail: String): String {
        val claims: MutableMap<String, Any> = hashMapOf("email" to userEmail)
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)
        return tokenPrefix + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }
    fun getAuthenticationTokenFromJwt(token: String): Authentication {
        var tokenWithoutPrefix = token
        tokenWithoutPrefix = removePrefix(tokenWithoutPrefix)
        val claims = Jwts.parser()
            .setSigningKey(jwtSecretKey)
            .parseClaimsJws(tokenWithoutPrefix)
            .body

        // Recover User class from JWT
        val email = claims.get("email", String::class.java)
        val currentUser = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("$email is not valid email, check token is expired")
        val userPrincipal = UserPrincipal(currentUser)
        val authorises = userPrincipal.authorities
        // Make token with parsed data
        return CustomAuthenticationToken(userPrincipal, null, authorises)
    }

    fun validateToken(authToken: String?): Boolean {
        if(authToken.isNullOrEmpty()){
            logger.error("Token is not provided")
            return false
        }
        if (!authToken.startsWith(tokenPrefix)) {
            logger.error("Token not match type Bearer")
            return false
        }
        val authTokenWithoutPrefix = removePrefix(authToken)
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authTokenWithoutPrefix)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    fun removePrefix(tokenWithPrefix: String): String {
        return tokenWithPrefix.replace(tokenPrefix, "").trim { it <= ' ' }
    }
}