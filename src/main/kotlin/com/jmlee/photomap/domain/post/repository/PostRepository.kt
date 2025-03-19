package com.jmlee.photomap.domain.post.repository

import com.jmlee.photomap.domain.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findPostById(id: Long): Post?
    @Query("SELECT p FROM Post p JOIN p.hashtags h WHERE h.name = :name")
    fun getPostsByHashtag(name: String): List<Post>?
}