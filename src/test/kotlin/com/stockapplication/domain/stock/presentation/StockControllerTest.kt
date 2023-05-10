package com.stockapplication.domain.stock.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import com.stockapplication.domain.stock.application.model.StockResult
import com.stockapplication.domain.stock.infrastructure.StockRepository
import com.stockapplication.global.extension.toLocalDate
import com.stockapplication.infrastructure.finance.feign.YahooFinanceClient
import com.stockapplication.test.SpringIntegrationBDDSpec
import com.stockapplication.utils.finance.createStockResult
import com.stockapplication.utils.finance.createYahooChartResponse
import com.stockapplication.utils.finance.minusDays
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.time.Instant

class StockControllerTest(
  @MockkBean val yahooFinanceClient: YahooFinanceClient,
  val mockMvc: MockMvc,
  val objectMapper: ObjectMapper,
  val stockRepository: StockRepository,
) : SpringIntegrationBDDSpec(
  {
    feature("삼성 주식 정보를 저장한다") {
      scenario("최근 5일치 정보를 저장한다") {
        // given
        val uri = "/api/v1/stocks/samsung"
        val now = Instant.now()
        val timestamps =
          listOf(now, now.minusDays(1), now.minusDays(2), now.minusDays(3), now.minusDays(4))
        every {
          yahooFinanceClient.fetchFinanceChart(
            CODE,
            range = "5d",
          )
        } returns createYahooChartResponse(
          code = CODE,
          timeZone = TIME_ZONE_NAME,
          timestamp = timestamps,
          open = listOf(1.0, 1.1, 1.2, 1.3, 1.4),
          close = listOf(2.0, 2.1, 2.2, 2.3, 2.4),
          volume = listOf(100, 200, 300, 400, 500),
          high = listOf(2.1, 2.2, 2.3, 2.4, 2.5),
          low = listOf(0.8, 0.9, 1.0, 1.1, 1.2),
        )

        // when
        val result = mockMvc.perform(
          MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON),
        ).andReturn().response

        // then
        val response = objectMapper.readValue<List<StockResult>>(result.contentAsString)
        response shouldBe listOf(
          createStockResult(2.5, 1.2, 500, 1.4, 2.4, timestamps[4].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(2.4, 1.1, 400, 1.3, 2.3, timestamps[3].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(2.3, 1.0, 300, 1.2, 2.2, timestamps[2].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(2.2, 0.9, 200, 1.1, 2.1, timestamps[1].toLocalDate(TIME_ZONE_NAME)),
          createStockResult(2.1, 0.8, 100, 1.0, 2.0, timestamps[0].toLocalDate(TIME_ZONE_NAME)),
        )
        stockRepository.findAll().shouldHaveSize(5)
      }
    }
  },
) {
  companion object {
    private const val CODE = "005930.KS"
    private const val TIME_ZONE_NAME = "Asia/Seoul"
  }
}
