package com.jmlee.photomap.domain.post.service

import com.jmlee.photomap.domain.picture.dto.PictureDto
import com.jmlee.photomap.domain.picture.model.Picture
import com.jmlee.photomap.domain.picture.repository.PictureRepository
import com.jmlee.photomap.domain.picture.service.PictureService
import com.jmlee.photomap.domain.post.dto.PostDto
import com.jmlee.photomap.domain.post.exception.PostNotFoundException
import com.jmlee.photomap.domain.post.model.Post
import com.jmlee.photomap.domain.post.repository.PostRepository
import com.jmlee.photomap.domain.user.exception.UserNotAllowedException
import com.jmlee.photomap.domain.user.exception.UserNotFoundException
import com.jmlee.photomap.domain.user.model.User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PostService(
    val postRepository: PostRepository,
    val pictureService: PictureService,
    val pictureRepository: PictureRepository
) {
    fun create(
        user: User,
        postCreateRequest: PostDto.CreateRequest,
        pictureCreateRequests: List<PictureDto.CreateRequest>
    ): Post {
        val pictures : MutableList<Picture> = pictureCreateRequests.map { pictureService.create(it)}.toMutableList()
        val newPost = Post(postCreateRequest, user, pictures)
        user.posts.add(newPost)
        pictures.forEach { it.post = newPost }
        return postRepository.save(newPost)
    }

    fun view(id: Long): Post{
        val post = postRepository.findPostById(id) ?: throw PostNotFoundException()
        return post
    }

    @Transactional
    fun edit(user: User, id: Long, postEditRequest: PostDto.EditRequest, pictureEditRequests: List<PictureDto.EditRequest>, pictureCreateRequests: List<PictureDto.CreateRequest>): Post {
        val post = user.posts.find { it.id == id } ?: throw UserNotAllowedException()
        post.edit(postEditRequest)
        val pictureEditRequestIds = pictureEditRequests.map { it.id }.toSet()
        val picturesToDelete = post.pictures.filter { it.id !in pictureEditRequestIds }
        for (picture in post.pictures){
            val pictureEditRequest = pictureEditRequests.find { it.id == picture.id }
            if (pictureEditRequest != null) {
                picture.description = pictureEditRequest.description
                picture.longitude = pictureEditRequest.coordinate[0]
                picture.latitude = pictureEditRequest.coordinate[1]
                pictureRepository.save(picture)
            }
        }
        pictureRepository.deleteAllInBatch(picturesToDelete)
        val pictures : MutableList<Picture> = pictureCreateRequests.map { pictureService.create(it)}.toMutableList()
        post.pictures.addAll(pictures)
        post.pictures.forEach { it.post = post }
        return postRepository.save(post)
    }

    fun delete(user: User, id: Long) {
        val post = user.posts.find { it.id == id } ?: throw UserNotAllowedException()
        postRepository.delete(post)
    }
}