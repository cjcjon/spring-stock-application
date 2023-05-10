package com.stockapplication.infrastructure.finance

import com.stockapplication.infrastructure.finance.feign.YahooFinanceClient
import com.stockapplication.infrastructure.finance.model.StockResponse
import org.springframework.stereotype.Component

@Component
class YahooFinanceAdaptor(private val client: YahooFinanceClient) : FinanceAdaptor {

  override fun fetchDailyStock(code: String, range: Int): List<StockResponse> {
    val response = client.fetchFinanceChart(code = code, range = "${range}d")

    return StockResponse.from(response)
  }
}
