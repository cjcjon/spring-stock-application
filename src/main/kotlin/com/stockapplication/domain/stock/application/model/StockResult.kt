package com.stockapplication.domain.stock.application.model

import com.stockapplication.domain.stock.domain.Stock
import java.time.LocalDate

data class StockResult(
  val high: Double,
  val low: Double,
  val volume: Long,
  val open: Double,
  val close: Double,
  val date: LocalDate,
) {
  companion object {
    fun from(stock: Stock) = StockResult(
      high = stock.high,
      low = stock.low,
      volume = stock.volume,
      open = stock.open,
      close = stock.close,
      date = stock.date,
    )
  }
}
