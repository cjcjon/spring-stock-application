package com.stockapplication.global.utils

import com.stockapplication.global.exception.InvalidRequestException
import com.stockapplication.global.utils.ValidationUtil.cond
import com.stockapplication.test.BaseUnitSpec
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe

class ValidationUtilSpec : BaseUnitSpec(
  {
    context("조건부 검사 함수") {
      test("조건이 성공할경우 값을 반환한다") {
        // when
        val result = cond(true, 1) { InvalidRequestException() }

        // then
        result shouldBe 1
      }

      test("조건이 실패할경우 오류를 던진다") {
        // then
        shouldThrowExactly<InvalidRequestException> {
          // when
          cond(false, 1) { InvalidRequestException() }
        }
      }
    }
  },
)
