package com.jmlee.photomap.domain.post.repository

import com.jmlee.photomap.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findPostById(id: Long): Post?
}