package com.jmlee.photomap.domain.user.repository

import com.jmlee.photomap.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByUsername(username: String?): User?
}

