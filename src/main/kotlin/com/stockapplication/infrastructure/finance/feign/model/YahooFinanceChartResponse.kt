package com.stockapplication.infrastructure.finance.feign.model

import java.time.Instant

data class YahooFinanceChartResponse(val chart: ChartResult) {

  data class ChartResult(val result: List<FinanceResult>)

  data class FinanceResult(val meta: MetaResult, val timestamp: List<Instant>, val indicators: IndicatorResult)

  data class MetaResult(val symbol: String, val exchangeTimezoneName: String)

  data class IndicatorResult(val quote: List<QuoteResult>)

  data class QuoteResult(
    val open: List<Double>,
    val close: List<Double>,
    val volume: List<Long>,
    val high: List<Double>,
    val low: List<Double>,
  )
}
