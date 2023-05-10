package com.stockapplication.infrastructure.finance.model

import com.stockapplication.global.extension.toLocalDate
import com.stockapplication.global.extension.zipListAll
import com.stockapplication.infrastructure.finance.feign.model.YahooFinanceChartResponse
import java.time.LocalDate

data class StockResponse(
  val code: String,
  val high: Double,
  val low: Double,
  val open: Double,
  val close: Double,
  val volume: Long,
  val date: LocalDate,
) {

  companion object {

    fun from(chartResponse: YahooFinanceChartResponse): List<StockResponse> =
      chartResponse.chart.result.firstOrNull()?.run {
        indicators.quote.firstOrNull()?.run {
          open.zipListAll(close, high, low, volume.map { it.toDouble() }).zip(timestamp).map {
            StockResponse(
              code = meta.symbol,
              open = it.first[0],
              close = it.first[1],
              high = it.first[2],
              low = it.first[3],
              volume = it.first[4].toLong(),
              date = it.second.toLocalDate(meta.exchangeTimezoneName),
            )
          }
        }
      } ?: emptyList()
  }
}
