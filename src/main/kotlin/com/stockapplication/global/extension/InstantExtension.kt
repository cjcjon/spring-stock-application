package com.stockapplication.global.extension

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Instant.toLocalDate(zoneName: String): LocalDate =
  this.atZone(ZoneId.of(zoneName)).toLocalDate()
