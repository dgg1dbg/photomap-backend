package com.jmlee.photomap.global.config

import com.jmlee.photomap.domain.user.dto.UserDto
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val newSignUpRequest = UserDto.SignUpRequest("jlee", "jeongminx@snu.ac.kr", "aass1223", "hi")
        val newUser = User(newSignUpRequest, passwordEncoder)
        userRepository.save(newUser)
    }
}