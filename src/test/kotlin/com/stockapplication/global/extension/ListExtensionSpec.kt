package com.stockapplication.global.extension

import com.stockapplication.test.BaseUnitSpec
import io.kotest.matchers.shouldBe

class ListExtensionSpec: BaseUnitSpec({
  context("리스트 여러개를 Index 마다 리스트로 묶인 이중 리스트로 합친다") {
    test("리스트 단건을 이중 리스트로 합칠경우 빈 리스트로 합쳐진다") {
      // given
      val list = listOf(1)

      // when
      val result = list.zipListAll()

      // then
      result shouldBe emptyList()
    }

    test("리스트 두건을 이중 리스트로 합칠경우 이중 리스트로 합쳐진다") {
      // given
      val list1 = listOf(1, 2, 3)
      val list2 = listOf(4, 5)

      // when
      val result = list1.zipListAll(list2)

      // then
      result shouldBe listOf(listOf(1, 4), listOf(2, 5))
    }

    test("리스트 여러건을 이중 리스트로 합친다") {
      // given
      val list1 = listOf(1, 2)
      val list2 = listOf(3)
      val list3 = listOf(4, 5, 6)

      // when
      val result = list1.zipListAll(list2, list3)

      // then
      result shouldBe listOf(listOf(1, 3, 4))
    }
  }
})
