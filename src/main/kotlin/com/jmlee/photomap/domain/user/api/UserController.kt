package com.jmlee.photomap.domain.user.api

import com.jmlee.photomap.domain.user.dto.UserDto
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.domain.user.repository.UserRepository
import com.jmlee.photomap.domain.user.service.UserService
import com.jmlee.photomap.global.auth.CurrentUser
import com.jmlee.photomap.global.auth.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/signup")
    fun signup(@RequestBody signupRequest: UserDto.SignUpRequest): ResponseEntity<UserDto.Response>{
        val user = userService.create(signupRequest)
        return ResponseEntity.noContent().header("Authentication", jwtTokenProvider.generateToken(user.email)).build()
    }
    @PostMapping("/signin")
    fun signin(@CurrentUser user: User): UserDto.Response{
        return UserDto.Response(user)
    }
    @PutMapping("/edit")
    fun edit(@CurrentUser user: User, @RequestBody editRequest: UserDto.EditRequest): UserDto.Response{
        val user = userService.edit(user, editRequest)
        return UserDto.Response(user)
    }
    @GetMapping("/view")
    fun view(@CurrentUser user: User): UserDto.ViewResponse{
        return UserDto.ViewResponse(user)
    }
    @GetMapping("/view/{username}")
    fun viewUser(@PathVariable username: String): UserDto.ViewResponse{
        val user = userService.view(username)
        return UserDto.ViewResponse(user)
    }
    @DeleteMapping("/delete")
    fun delete(@RequestBody deleteRequest: UserDto.DeleteRequest): UserDto.DeleteResponse{
        userService.delete(deleteRequest)
        return UserDto.DeleteResponse()
    }

}