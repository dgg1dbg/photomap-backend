package com.jmlee.photomap.global.config

import com.jmlee.photomap.global.auth.JwtAuthenticationEntryPoint
import com.jmlee.photomap.global.auth.JwtAuthenticationFilter
import com.jmlee.photomap.global.auth.JwtTokenProvider
import com.jmlee.photomap.global.auth.SignInLoginFilter
import com.jmlee.photomap.global.auth.model.UserPrincipalDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userPrincipalDetailService: UserPrincipalDetailService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
){
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authManagerBuilder.userDetailsService(userPrincipalDetailService)
        .passwordEncoder(passwordEncoder())

        return authManagerBuilder.build()
    }

    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager,
                    jwtTokenProvider: JwtTokenProvider
    ): SecurityFilterChain {
        val signInLoginFilter = SignInLoginFilter(authenticationManager, jwtTokenProvider)
        val jwtAuthenticationFilter = JwtAuthenticationFilter(authenticationManager, jwtTokenProvider)
        http.csrf {csrf->csrf.disable()}
            .formLogin{formLogin -> formLogin.disable()}
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint) }
            .addFilter(signInLoginFilter)
            .addFilter(jwtAuthenticationFilter)
            .authorizeHttpRequests {
                it.requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/picture/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/user/signup").anonymous()
                it.requestMatchers(HttpMethod.DELETE, "/api/user/delete").hasAuthority("admin")
                it.anyRequest().authenticated()
            }
        return http.build()
    }
}