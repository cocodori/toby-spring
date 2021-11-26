package com.tobybook.exception

class DuplicateUserIdException(
    override val cause: Throwable
): RuntimeException(cause)