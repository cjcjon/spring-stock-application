package com.stockapplication.global.utils

object ValidationUtil {
  fun <T> cond(test: Boolean, value: T, error: () -> Throwable) =
    if (test) value
    else throw error()
}
