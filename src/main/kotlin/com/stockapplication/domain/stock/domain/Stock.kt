package com.stockapplication.domain.stock.domain

import com.stockapplication.global.entity.BaseEntity
import com.stockapplication.infrastructure.finance.model.StockResponse
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "Stock", uniqueConstraints = [UniqueConstraint(columnNames = ["date"])])
class Stock(
  code: String,
  high: Double,
  low: Double,
  open: Double,
  close: Double,
  volume: Long,
  date: LocalDate,
) : BaseEntity() {

  @field:Column(nullable = false)
  var code: String = code
    private set

  @field:Column(nullable = false)
  var high: Double = high
    private set

  @field:Column(nullable = false)
  var low: Double = low
    private set

  @field:Column(nullable = false)
  var open: Double = open
    private set

  @field:Column(nullable = false)
  var close: Double = close
    private set

  @field:Column(nullable = false)
  var volume: Long = volume
    private set

  @field:Column(nullable = false)
  var date: LocalDate = date
    private set

  fun updateChart(
    high: Double,
    low: Double,
    open: Double,
    close: Double,
    volume: Long,
  ) = this.apply {
    this.high = high
    this.low = low
    this.open = open
    this.close = close
    this.volume = volume
  }

  companion object {
    fun from(stockResponse: StockResponse) = Stock(
      code = stockResponse.code,
      high = stockResponse.high,
      low = stockResponse.low,
      open = stockResponse.open,
      close = stockResponse.close,
      volume = stockResponse.volume,
      date = stockResponse.date,
    )
  }
}
