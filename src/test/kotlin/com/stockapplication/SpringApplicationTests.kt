package com.stockapplication

import com.stockapplication.test.SpringIntegrationBDDSpec
import io.kotest.matchers.shouldBe
import org.springframework.transaction.annotation.Transactional

@Transactional
class SpringApplicationTests : SpringIntegrationBDDSpec({
  feature("스프링을 실행한다") {
    scenario("스프링의 context가 정상적으로 불러와진다") {
      true shouldBe true
    }
  }
})
