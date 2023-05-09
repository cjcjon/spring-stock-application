package com.spring.global.exception

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

sealed class ApiException(
  val status: HttpStatus,
  override val message: String,
  override val cause: Throwable?,
) : RuntimeException(message, cause)

data class InvalidRequestException(
  override val message: String = "유효하지 않은 요청입니다",
  override val cause: Throwable? = null,
) : ApiException(HttpStatus.BAD_REQUEST, message, cause)

data class NotFoundException(
  override val message: String,
  override val cause: Throwable? = null,
) : ApiException(HttpStatus.NOT_FOUND, message, cause)
