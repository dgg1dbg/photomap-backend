package com.jmlee.photomap.domain.user.exception

import com.jmlee.photomap.global.common.exception.DataNotFoundException
import com.jmlee.photomap.global.common.exception.ErrorType

class UserNotFoundException : DataNotFoundException(
    errorType = ErrorType.USER_NOT_FOUND,
    detail = "User not found"
)