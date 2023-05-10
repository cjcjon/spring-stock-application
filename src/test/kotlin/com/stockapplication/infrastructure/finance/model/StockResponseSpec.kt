package com.stockapplication.infrastructure.finance.model

import com.stockapplication.global.extension.toLocalDate
import com.stockapplication.infrastructure.finance.feign.model.YahooFinanceChartResponse
import com.stockapplication.test.BaseUnitSpec
import com.stockapplication.utils.finance.createYahooChartResponse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import java.time.Instant

class StockResponseSpec : BaseUnitSpec(
  {
    context("야후의 차트 정보를 통해 변환한다") {
      test("차트 정보가 없을경우 빈 값을 반환한다") {
        // given
        val emptyResponse = YahooFinanceChartResponse(
          chart = YahooFinanceChartResponse.ChartResult(
            result = emptyList(),
          ),
        )

        // when
        val result = StockResponse.from(emptyResponse)

        // then
        result.shouldBeEmpty()
      }

      test("시세 정보가 없을경우 빈 값을 반환한다") {
        // given
        val emptyQuoteResponse = YahooFinanceChartResponse(
          chart = YahooFinanceChartResponse.ChartResult(
            result = listOf(
              YahooFinanceChartResponse.FinanceResult(
                meta = YahooFinanceChartResponse.MetaResult(
                  symbol = "symbol",
                  exchangeTimezoneName = "Asia/Seoul",
                ),
                timestamp = listOf(Instant.MAX),
                indicators = YahooFinanceChartResponse.IndicatorResult(quote = emptyList()),
              ),
            ),
          ),
        )

        // when
        val result = StockResponse.from(emptyQuoteResponse)

        // then
        result.shouldBeEmpty()
      }

      test("시간 정보가 없을경우 빈 값을 반환한다") {
        // given
        val emptyTimeResponse = createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          open = listOf(1.0),
          close = listOf(1.0),
          volume = listOf(1),
          high = listOf(1.0),
          low = listOf(1.0),
        )

        // when
        val result = StockResponse.from(emptyTimeResponse)

        // then
        result.shouldBeEmpty()
      }

      test("차트 정보를 변환한다") {
        // given
        val now = Instant.now()
        val response = createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          timestamp = listOf(now),
          open = listOf(1.0),
          close = listOf(2.0),
          volume = listOf(3),
          high = listOf(4.0),
          low = listOf(5.0),
        )

        // when
        val result = StockResponse.from(response)

        // then
        result shouldBe listOf(
          StockResponse(
            code = CODE,
            open = 1.0,
            close = 2.0,
            volume = 3,
            high = 4.0,
            low = 5.0,
            date = now.toLocalDate(TIME_ZONE_NAME),
          ),
        )
      }
    }
  },
) {
  companion object {
    private const val CODE = "symbol"
    private const val TIME_ZONE_NAME = "Asia/Seoul"
  }
}
