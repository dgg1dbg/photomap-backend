package com.jmlee.photomap.domain.hashtag.exception

import com.jmlee.photomap.global.common.exception.ErrorType
import com.jmlee.photomap.global.common.exception.InvalidRequestException

class HashtagAlreadyExistsException: InvalidRequestException(
    errorType = ErrorType.HASHTAG_ALREADY_EXISTS,
    detail = "Hashtag Already Exists"
)