package com.spring.test

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureMockMvc
abstract class SpringIntegrationBDDSpec(body: FeatureSpec.() -> Unit = {}) : BaseBDDSpec(body) {
  override fun extensions() = listOf(SpringExtension)
}
