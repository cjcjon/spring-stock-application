package com.spring.test

import io.kotest.core.spec.style.FeatureSpec
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
abstract class BaseBDDSpec(body: FeatureSpec.() -> Unit = {}) : FeatureSpec(body)
