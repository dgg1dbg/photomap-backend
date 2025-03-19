package com.jmlee.photomap.domain.user.service

import com.jmlee.photomap.domain.user.dto.UserDto
import com.jmlee.photomap.domain.user.exception.UserAlreadyExistsException
import com.jmlee.photomap.domain.user.exception.UserNotFoundException
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(createRequest: UserDto.SignUpRequest): User {
        if(userRepository.findByEmail(createRequest.email) != null) {
            throw UserAlreadyExistsException()
        }
        if(userRepository.findByUsername(createRequest.username) != null) {
            throw UserAlreadyExistsException()
        }
        val newUser = User(createRequest, passwordEncoder)
        return userRepository.save(newUser)
    }
    @Transactional
    fun edit(user: User, editRequest: UserDto.UserEditRequest): User {
        if(user.username != editRequest.username && userRepository.findByUsername(editRequest.username) != null) {
            throw UserAlreadyExistsException()
        }
        user.update(editRequest.username, editRequest.password, editRequest.description, passwordEncoder)
        return userRepository.save(user)
    }
    fun view(username: String): User {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        return user

    }
    fun delete(deleteRequest: UserDto.UserDeleteRequest) {
        val user = userRepository.findByUsername(deleteRequest.username) ?: throw UserNotFoundException()
        userRepository.delete(user)
    }
}