package com.stockapplication.global.exception.advice

import com.stockapplication.global.exception.ApiException
import com.stockapplication.global.exception.ErrorCode
import com.stockapplication.global.exception.ErrorResponse
import feign.FeignException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

val logger = KotlinLogging.logger {}

@RestControllerAdvice
class ExceptionHandlerAdvice {

  @ExceptionHandler
  fun apiExceptionHandler(exception: ApiException): ResponseEntity<ErrorResponse> {
    logError(exception)

    val response = ErrorResponse(code = ErrorCode.CLIENT_ERROR, message = exception.message)
    return ResponseEntity(response, exception.status)
  }

  @ExceptionHandler
  fun notFoundHandler(exception: NoHandlerFoundException): ResponseEntity<ErrorResponse> {
    logError(exception)

    val response = ErrorResponse(code = ErrorCode.NOT_FOUND, message = "요청 url을 찾을 수 없습니다")
    return ResponseEntity(response, HttpStatus.NOT_FOUND)
  }

  @ExceptionHandler
  fun feignExceptionHandler(exception: FeignException): ResponseEntity<ErrorResponse> {
    logError(exception)

    val response = ErrorResponse(code = ErrorCode.SERVER_ERROR, message = "서버 에러입니다")
    return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler
  fun exceptionHandler(exception: Exception): ResponseEntity<ErrorResponse> {
    logError(exception)

    val response = ErrorResponse(code = ErrorCode.SERVER_ERROR, message = "서버 에러입니다")
    return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
  }

  private fun logError(exception: Exception) {
    logger.error(exception) { "[ExceptionHandler] ex" }
  }
}
