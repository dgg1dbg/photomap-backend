package com.jmlee.photomap.domain.hashtag.model
import jakarta.persistence.*
import com.jmlee.photomap.domain.model.BaseTimeEntity
import com.jmlee.photomap.domain.post.model.Post

@Entity
class Hashtag(
    @Column
    val name: String,
    @ManyToMany(mappedBy = "hashtags")
    var posts: MutableList<Post>,

): BaseTimeEntity() {
    constructor(name: String) : this(
        name = name,
        posts = mutableListOf<Post>()
    )

    fun addPost(post: Post) {
        posts.add(post)
    }
    fun removePost(post: Post) {
        posts.remove(post)
    }
}