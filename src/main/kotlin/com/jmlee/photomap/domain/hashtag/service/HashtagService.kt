package com.jmlee.photomap.domain.hashtag.service

import org.springframework.stereotype.Service
import com.jmlee.photomap.domain.hashtag.repository.HashtagRepository
import com.jmlee.photomap.domain.picture.repository.PictureRepository
import com.jmlee.photomap.domain.post.repository.PostRepository
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.hashtag.exception.HashtagAlreadyExistsException
import com.jmlee.photomap.domain.hashtag.model.Hashtag

@Service
class HashtagService(
    val hashtagRepository: HashtagRepository
) {
    fun getPosts(hashtag: String): List<Post>? {
        return hashtagRepository.getPosts(hashtag.lowercase())
    }
    fun getPictures(hashtag: String): List<Picture>? {
        val posts = hashtagRepository.getPosts(hashtag.lowercase())
        val pictures = mutableListOf<Picture>()
        posts?.forEach { pictures.addAll(it.pictures) }
        return pictures
    }
    fun create(hashtag: String): Hashtag {
        if (hashtagRepository.getHashtag(hashtag.lowercase())!!.isNotEmpty()) {
            throw HashtagAlreadyExistsException()
        }
        val newHashtag = Hashtag(hashtag.lowercase(), mutableListOf<Post>())
        return hashtagRepository.save(newHashtag)
    }
    fun search(hashtag: String): List<Hashtag>? {
        return hashtagRepository.getHashtag(hashtag.lowercase())
    }
    fun getTrending(count: Int): List<Hashtag>? {
        return hashtagRepository.getTrending(count)
    }
}