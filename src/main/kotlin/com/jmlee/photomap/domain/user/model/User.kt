package com.jmlee.photomap.domain.user.model

import com.jmlee.photomap.domain.model.BaseEntity
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.user.dto.UserDto
import jakarta.persistence.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
class User (
    @Column
    var username: String,
    @Column
    val email: String,
    @Column
    var password: String,
    @Column
    var roles: String,

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE])
    var posts: MutableList<Post>,
    @Column
    var description: String
): BaseEntity() {
    constructor(
        signUpRequest: UserDto.SignUpRequest,
        passwordEncoder: PasswordEncoder
        ): this(
        username = signUpRequest.username,
        email = signUpRequest.email,
        password = passwordEncoder.encode(signUpRequest.password),
        roles = if (signUpRequest.email == "jeongminx@snu.ac.kr") "admin,user" else "user",
        posts = mutableListOf<Post>(),
        description = signUpRequest.description ?: ""
    )
    fun update(username: String?, password: String?, description: String?, passwordEncoder: PasswordEncoder){
        this.username = username ?: this.username
        this.password = if (password != null) passwordEncoder.encode(password) else this.password
        this.description = description ?: this.description
    }
}