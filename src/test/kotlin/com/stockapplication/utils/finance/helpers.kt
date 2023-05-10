package com.stockapplication.utils.finance

import com.stockapplication.domain.stock.application.model.StockResult
import com.stockapplication.infrastructure.finance.feign.model.YahooFinanceChartResponse
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun createYahooChartResponse(
  code: String,
  timeZone: String,
  timestamp: List<Instant> = emptyList(),
  open: List<Double> = emptyList(),
  close: List<Double> = emptyList(),
  volume: List<Long> = emptyList(),
  high: List<Double> = emptyList(),
  low: List<Double> = emptyList(),
) = YahooFinanceChartResponse(
  chart = YahooFinanceChartResponse.ChartResult(
    result = listOf(
      YahooFinanceChartResponse.FinanceResult(
        meta = YahooFinanceChartResponse.MetaResult(
          symbol = code,
          exchangeTimezoneName = timeZone,
        ),
        timestamp = timestamp,
        indicators = YahooFinanceChartResponse.IndicatorResult(
          quote = listOf(
            YahooFinanceChartResponse.QuoteResult(
              open = open,
              close = close,
              volume = volume,
              high = high,
              low = low,
            ),
          ),
        ),
      ),
    ),
  ),
)

fun Instant.minusDays(n: Long): Instant = this.minus(n, ChronoUnit.DAYS)

fun createStockResult(
  high: Double,
  low: Double,
  volume: Long,
  open: Double,
  close: Double,
  date: LocalDate,
): StockResult = StockResult(
  high = high,
  low = low,
  volume = volume,
  open = open,
  close = close,
  date = date,
)
