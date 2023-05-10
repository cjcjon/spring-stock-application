package com.stockapplication.domain.stock.domain

import com.stockapplication.global.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Stock")
class Stock(
  code: String,
  high: Double,
  low: Double,
  open: Double,
  close: Double,
  volume: Double,
  transactionDate: LocalDateTime,
) : BaseEntity() {

  var code: String = code
    private set

  var high: Double = high
    private set

  var low: Double = low
    private set

  var open: Double = open
    private set

  var close: Double = close
    private set

  var volume: Double = volume
    private set

  var transactionDate: LocalDateTime = transactionDate
    private set
}
