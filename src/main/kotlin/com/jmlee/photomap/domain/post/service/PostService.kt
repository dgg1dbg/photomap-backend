package com.jmlee.photomap.domain.post.service

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.picture.repository.PictureRepository
import com.jmlee.photomap.domain.picture.service.PictureService
import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.post.exception.PostNotFoundException
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.post.repository.PostRepository
import com.jmlee.photomap.domain.hashtag.repository.HashtagRepository
import com.jmlee.photomap.domain.user.exception.UserNotAllowedException
import com.jmlee.photomap.domain.user.model.User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class PostService(
    val postRepository: PostRepository,
    val pictureService: PictureService,
    val pictureRepository: PictureRepository,
    val hashtagRepository: HashtagRepository
) {
    fun create(
        user: User,
        postCreateRequest: PostDto.PostCreateRequest,
        pictureCreateRequests: List<PictureDto.PictureCreateRequest>
    ): Post {
        val pictures : MutableList<Picture> = pictureCreateRequests.map { pictureService.create(it)}.toMutableList()
        val newPost = Post(postCreateRequest, hashtagRepository, user, pictures)
        user.posts.add(newPost)
        pictures.forEach { it.post = newPost }
        return postRepository.save(newPost)
    }

    fun view(id: Long): Post{
        val post = postRepository.findPostById(id) ?: throw PostNotFoundException()
        return post
    }

    @Transactional
    fun edit(user: User, id: Long, postEditRequest: PostDto.PostEditRequest, pictureEditRequests: List<PictureDto.PictureEditRequest>): Post {
        val post = user.posts.find { it.id == id } ?: throw UserNotAllowedException()
        post.edit(postEditRequest, hashtagRepository)
        val pictures : MutableList<Picture> = pictureEditRequests.mapNotNull { 
            when (it.file) {
                is MultipartFile -> {
                    pictureService.create(PictureDto.PictureCreateRequest(it.description, it.file, it.coordinate))
                }
                is String -> {
                    val picture = pictureRepository.findByPictureByFileDir(it.file)
                    picture?.description = it.description
                    if (it.coordinate.size >= 2) { 
                        picture?.longitude = it.coordinate[0]
                        picture?.latitude = it.coordinate[1]
                    }
                    pictureRepository.save(picture!!)
                }
                else -> null
            }
        }.toMutableList()

        val picturesToDelete = post.pictures.filter { existing -> 
            pictures.none {it.id == existing.id}
        }
        
        picturesToDelete.forEach { picture ->
            pictureRepository.delete(picture)
        }

        pictures.forEach {it.post = post}
        return postRepository.save(post)
    }

    fun delete(user: User, id: Long) {
        val post = user.posts.find { it.id == id } ?: throw UserNotAllowedException()
        postRepository.delete(post)
    }
}