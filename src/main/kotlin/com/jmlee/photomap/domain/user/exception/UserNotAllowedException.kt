package com.jmlee.photomap.domain.user.exception

import com.jmlee.photomap.global.common.exception.ErrorType
import com.jmlee.photomap.global.common.exception.NotAllowedException

class UserNotAllowedException : NotAllowedException(
    errorType = ErrorType.USER_NOT_ALLOWED,
    detail = "User not allowed."
)