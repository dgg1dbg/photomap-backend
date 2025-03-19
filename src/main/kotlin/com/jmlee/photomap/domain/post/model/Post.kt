package com.jmlee.photomap.domain.post.model

import com.jmlee.photomap.domain.model.BaseTimeEntity
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.user.model.User
import com.jmlee.photomap.domain.hashtag.model.Hashtag
import com.jmlee.photomap.domain.hashtag.repository.HashtagRepository
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Post(
    @Column
    var name: String,
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "post_hashtag",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "hashtag_id")]
    )
    var hashtags: MutableList<Hashtag> = mutableListOf(),
    @Column
    var date: LocalDate,
    @Column
    var time: LocalTime,
    @Column
    var description: String,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user")
    val user: User,
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], orphanRemoval = true)
    var pictures: MutableList<Picture>,
): BaseTimeEntity() {
    constructor(postCreateRequest: PostDto.PostCreateRequest, hashtagRepository: HashtagRepository, user: User, pictures: MutableList<Picture>) : this(
        name = postCreateRequest.name,
        hashtags = postCreateRequest.hashtag
            .split("#")
            .filter { it.isNotBlank() }
            .mapNotNull { hashtagRepository.getExactHashtag(it) }
            .toMutableList(),
        date = postCreateRequest.date,
        time = postCreateRequest.time,
        description = postCreateRequest.description,
        user = user,
        pictures = pictures
    )
    fun edit(postEditRequest: PostDto.PostEditRequest, hashtagRepository: HashtagRepository) {
        name = postEditRequest.name
        hashtags = postEditRequest.hashtag
            .split("#")
            .filter { it.isNotBlank() }
            .mapNotNull { hashtagRepository.getExactHashtag(it) }
            .toMutableList()
        date = postEditRequest.date
        time = postEditRequest.time
        description = postEditRequest.description
    }

    @PreRemove
    fun deleteAssociatedFiles() {
        pictures.forEach { it.deleteFile() }
    }
}