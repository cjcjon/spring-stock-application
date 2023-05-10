package com.stockapplication.domain.stock.application

import com.stockapplication.domain.stock.application.model.StockResult
import com.stockapplication.domain.stock.domain.Stock
import com.stockapplication.domain.stock.infrastructure.StockRepository
import com.stockapplication.global.exception.InvalidRequestException
import com.stockapplication.global.extension.toLocalDate
import com.stockapplication.infrastructure.finance.YahooFinanceAdaptor
import com.stockapplication.infrastructure.finance.feign.YahooFinanceClient
import com.stockapplication.test.SpringBDDSpec
import com.stockapplication.utils.finance.createYahooChartResponse
import com.stockapplication.utils.finance.minusDays
import feign.codec.EncodeException
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk
import java.time.Instant
import java.time.LocalDate

class StockSaveServiceSpec(stockRepository: StockRepository) : SpringBDDSpec(
  {

    val yahooFinanceClient = mockk<YahooFinanceClient>()
    val financeAdaptor = YahooFinanceAdaptor(yahooFinanceClient)

    val sut = StockSaveService(financeAdaptor, stockRepository)

    feature("주식 정보를 저장한다") {
      scenario("빈 주식 코드를 입력할경우 실패한다") {
        // given
        val emptyCode = ""

        // then
        shouldThrowExactly<InvalidRequestException> {
          // when
          sut.saveDailyStocks(emptyCode, 1)
        }
      }

      scenario("범위 값이 0 이하일경우 실패한다") {
        // given
        checkAll(Arb.int(Int.MIN_VALUE, 0)) { range ->
          // then
          shouldThrowExactly<InvalidRequestException> {
            // when
            sut.saveDailyStocks(CODE, range)
          }
        }
      }

      scenario("주식 정보 요청에 실패할경우 저장에 실패한다") {
        // given
        val range = 2
        every {
          yahooFinanceClient.fetchFinanceChart(
            code = CODE,
            range = "2d",
          )
        } throws EncodeException("실패")

        // then
        shouldThrowExactly<EncodeException> {
          // when
          sut.saveDailyStocks(CODE, range)
        }
      }

      scenario("첫 요청일경우 주식 정보를 전체 저장한다") {
        // given
        val range = 3
        val now = Instant.now()
        val timestamps = listOf(now, now.minusDays(1), now.minusDays(2))

        every {
          yahooFinanceClient.fetchFinanceChart(
            code = CODE,
            range = "3d",
          )
        } returns createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          timestamp = timestamps,
          open = listOf(1.0, 1.1, 1.2),
          close = listOf(2.0, 2.1, 2.2),
          volume = listOf(100, 200, 300),
          high = listOf(1.5, 1.8, 1.4),
          low = listOf(0.8, 1.1, 1.2),
        )

        // when
        val result = sut.saveDailyStocks(CODE, range)

        // then
        result shouldBe listOf(
          createStockResult(1.4, 1.2, 300, 1.2, 2.2, timestamps[2].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(1.8, 1.1, 200, 1.1, 2.1, timestamps[1].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(1.5, 0.8, 100, 1.0, 2.0, timestamps[0].toLocalDate(TIME_ZONE_NAME)),
        )
        stockRepository.findAll().shouldHaveSize(3)
      }

      scenario("저장되지 않은 주식 정보를 추가적으로 저장한다") {
        // given
        val range = 2
        val now = Instant.now()
        val timestamps = listOf(now, now.minusDays(1))

        stockRepository.save(
          createStock(
            1.0, 1.0, 1, 1.0, 1.0,
            now.minusDays(1).toLocalDate(
              TIME_ZONE_NAME,
            ),
          ),
        )

        every {
          yahooFinanceClient.fetchFinanceChart(
            code = CODE,
            range = "2d",
          )
        } returns createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          timestamp = timestamps,
          open = listOf(1.0, 1.1),
          close = listOf(2.0, 2.1),
          volume = listOf(100, 200),
          high = listOf(1.5, 1.8),
          low = listOf(0.8, 1.1),
        )

        // when
        val result = sut.saveDailyStocks(CODE, range)

        // then
        result shouldBe listOf(
          createStockResult(1.8, 1.1, 200, 1.1, 2.1, timestamps[1].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(1.5, 0.8, 100, 1.0, 2.0, timestamps[0].toLocalDate(TIME_ZONE_NAME)),
        )
        stockRepository.findAll().shouldHaveSize(2)
      }

      scenario("변경된 주식 정보를 새로 갱신한다") {
        // given
        val range = 1
        val now = Instant.now()

        stockRepository.save(
          createStock(
            2.0, 2.0, 2, 2.0, 2.0, now.toLocalDate(TIME_ZONE_NAME),
          ),
        )

        every {
          yahooFinanceClient.fetchFinanceChart(
            code = CODE,
            range = "1d",
          )
        } returns createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          timestamp = listOf(now),
          open = listOf(1.0),
          close = listOf(1.0),
          volume = listOf(1),
          high = listOf(1.0),
          low = listOf(1.0),
        )

        // when
        val result = sut.saveDailyStocks(CODE, range)

        // then
        result shouldBe listOf(
          createStockResult(1.0, 1.0, 1, 1.0, 1.0, now.toLocalDate(TIME_ZONE_NAME)),
        )
        stockRepository.findAll().first().also {
          it.high shouldBe 1.0
          it.low shouldBe 1.0
          it.volume shouldBe 1
          it.open shouldBe 1.0
          it.close shouldBe 1.0
        }
      }
    }
  },
) {
  companion object {
    private const val CODE = "005930.KS"
    private const val TIME_ZONE_NAME = "Asia/Seoul"

    private fun createStockResult(
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

    private fun createStock(
      high: Double,
      low: Double,
      volume: Long,
      open: Double,
      close: Double,
      date: LocalDate,
      code: String = CODE,
    ) = Stock(
      code = code,
      high = high,
      low = low,
      volume = volume,
      open = open,
      close = close,
      date = date,
    )
  }
}
