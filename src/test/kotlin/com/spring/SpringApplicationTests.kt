package com.spring

import com.spring.test.SpringIntegrationBDDSpec
import io.kotest.matchers.shouldBe

class SpringApplicationTests : SpringIntegrationBDDSpec({
  feature("스프링을 실행한다") {
    scenario("스프링의 context가 정상적으로 불러와진다") {
      true shouldBe true
    }
  }
})
