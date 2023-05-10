package com.stockapplication.infrastructure.finance

import com.stockapplication.global.extension.toLocalDate
import com.stockapplication.infrastructure.finance.feign.YahooFinanceClient
import com.stockapplication.infrastructure.finance.model.StockResponse
import com.stockapplication.test.BaseBDDSpec
import com.stockapplication.utils.finance.createYahooChartResponse
import com.stockapplication.utils.finance.minusDays
import feign.codec.EncodeException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.Instant

class YahooFinanceAdaptorSpec : BaseBDDSpec(
  {
    val client = mockk<YahooFinanceClient>()
    val sut = YahooFinanceAdaptor(client)

    feature("일자별 주식 정보를 조회한다") {
      scenario("야후 주식 정보 호출에 실패할경우 조회에 실패한다") {
        // given
        val range = 3
        every { client.fetchFinanceChart(code = CODE, range = "${range}d") } throws EncodeException(
          "에러",
        )

        // then
        shouldThrowExactly<EncodeException> {
          // when
          sut.fetchDailyStock(CODE, range)
        }
      }

      scenario("야후 주식 정보를 조회한다") {
        // given
        val now = Instant.now()
        val range = 2
        every {
          client.fetchFinanceChart(
            code = CODE,
            range = "${range}d",
          )
        } returns createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          timestamp = listOf(now, now.minusDays(1)),
          open = listOf(1000.0, 2000.0),
          close = listOf(1100.0, 2100.0),
          volume = listOf(100, 200),
          high = listOf(1300.0, 2100.0),
          low = listOf(900.0, 2000.0),
        )

        // when
        val result = sut.fetchDailyStock(CODE, range)

        // then
        result shouldBe listOf(
          createStockResponse(1300.0, 900.0, 100, 1000.0, 1100.0, now),
          createStockResponse(2100.0, 2000.0, 200, 2000.0, 2100.0, now.minusDays(1)),
        )
      }
    }
  },
) {
  companion object {
    private const val CODE = "symbol"
    private const val TIME_ZONE_NAME = "Asia/Seoul"

    private fun createStockResponse(
      high: Double,
      low: Double,
      volume: Long,
      open: Double,
      close: Double,
      timestamp: Instant,
      code: String = CODE,
      timeZone: String = TIME_ZONE_NAME,
    ) = StockResponse(
      code = code,
      high = high,
      low = low,
      volume = volume,
      open = open,
      close = close,
      date = timestamp.toLocalDate(timeZone),
    )
  }
}
