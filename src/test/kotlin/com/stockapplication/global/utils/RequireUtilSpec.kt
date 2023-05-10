package com.stockapplication.global.utils

import com.stockapplication.global.exception.InvalidRequestException
import com.stockapplication.global.utils.RequireUtil.requireOrThrow
import com.stockapplication.test.BaseUnitSpec
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe

class RequireUtilSpec : BaseUnitSpec(
  {
    context("인수 검사 함수") {
      test("검사에 성공할경우 빈 값을 반환한다") {
        // when
        val result = requireOrThrow(true) { InvalidRequestException() }

        // then
        result shouldBe Unit
      }

      test("검사에 실패할경우 오류를 던진다") {
        // then
        shouldThrowExactly<InvalidRequestException> {
          // when
          requireOrThrow(false) { InvalidRequestException() }
        }
      }
    }
  },
)
