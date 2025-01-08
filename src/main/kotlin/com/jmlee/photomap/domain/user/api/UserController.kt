package com.jmlee.photomap.domain.user.api

import com.jmlee.photomap.domain.user.dto.UserDto
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.domain.user.service.UserService
import com.jmlee.photomap.global.auth.CurrentUser
import com.jmlee.photomap.global.auth.JwtTokenProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API", description = "User API")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    fun signup(@RequestBody signupRequest: UserDto.SignUpRequest): ResponseEntity<UserDto.UserResponse>{
        val user = userService.create(signupRequest)
        return ResponseEntity.noContent().header("Authentication", jwtTokenProvider.generateToken(user.email)).build()
    }
    @PostMapping("/signin")
    @Operation(summary = "Sign in")
    fun signin(@CurrentUser user: User): UserDto.UserResponse{
        return UserDto.UserResponse(user)
    }
    @PutMapping("/edit")
    @Operation(summary = "Edit your information")
    fun edit(@CurrentUser user: User, @RequestBody editRequest: UserDto.UserEditRequest): UserDto.UserResponse{
        val user = userService.edit(user, editRequest)
        return UserDto.UserResponse(user)
    }
    @GetMapping("/view")
    @Operation(summary = "View your information")
    fun view(@CurrentUser user: User): UserDto.UserViewResponse{
        return UserDto.UserViewResponse(user)
    }
    @GetMapping("/view/{username}")
    @Operation(summary = "View user information")
    fun viewUser(@PathVariable username: String): UserDto.UserViewResponse{
        val user = userService.view(username)
        return UserDto.UserViewResponse(user)
    }
    @DeleteMapping("/delete")
    @Operation(summary = "Delete your information")
    fun delete(@RequestBody deleteRequest: UserDto.UserDeleteRequest): UserDto.UserDeleteResponse{
        userService.delete(deleteRequest)
        return UserDto.UserDeleteResponse()
    }

}