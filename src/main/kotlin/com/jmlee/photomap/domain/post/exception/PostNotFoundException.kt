package com.jmlee.photomap.domain.post.exception

import com.jmlee.photomap.global.common.exception.DataNotFoundException
import com.jmlee.photomap.global.common.exception.ErrorType

class PostNotFoundException: DataNotFoundException(ErrorType.POST_NOT_FOUND, "Post not found")