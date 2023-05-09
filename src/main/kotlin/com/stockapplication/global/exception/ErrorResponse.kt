package com.stockapplication.global.exception

data class ErrorResponse(val code: ErrorCode, val message: String)

enum class ErrorCode {
  NOT_FOUND,
  CLIENT_ERROR,
  SERVER_ERROR,
}
