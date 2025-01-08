package com.jmlee.photomap.domain.post.model

import com.jmlee.photomap.domain.model.BaseTimeEntity
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.user.model.User
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Post(
    @Column
    var name: String,
    @Column
    var hashtag: String,
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
    constructor(postCreateRequest: PostDto.PostCreateRequest, user: User, pictures: MutableList<Picture>) : this(
        name = postCreateRequest.name,
        hashtag = postCreateRequest.hashtag,
        date = postCreateRequest.date,
        time = postCreateRequest.time,
        description = postCreateRequest.description,
        user = user,
        pictures = pictures
    )
    fun edit(postEditRequest: PostDto.PostEditRequest) {
        name = postEditRequest.name
        hashtag = postEditRequest.hashtag
        date = postEditRequest.date
        time = postEditRequest.time
        description = postEditRequest.description
    }

    @PreRemove
    fun deleteAssociatedFiles() {
        pictures.forEach { it.deleteFile() }
    }
}