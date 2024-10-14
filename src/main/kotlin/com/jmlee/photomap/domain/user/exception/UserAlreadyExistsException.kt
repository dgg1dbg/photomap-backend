package com.jmlee.photomap.domain.user.exception

import com.jmlee.photomap.global.common.exception.ErrorType
import com.jmlee.photomap.global.common.exception.InvalidRequestException

class UserAlreadyExistsException: InvalidRequestException(
    errorType = ErrorType.USER_ALREADY_EXISTS,
    detail = "User Already Exists"
)