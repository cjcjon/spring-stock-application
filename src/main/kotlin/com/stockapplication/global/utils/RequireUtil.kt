package com.stockapplication.global.utils

object RequireUtil {
  fun requireOrThrow(test: Boolean, error: () -> Throwable) =
    runCatching { require(test) }.getOrNull() ?: throw error()
}
