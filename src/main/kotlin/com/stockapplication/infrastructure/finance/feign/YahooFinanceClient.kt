package com.stockapplication.infrastructure.finance.feign

import com.stockapplication.infrastructure.finance.feign.model.YahooFinanceChartResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "YahooFinance", url = "\${external-api.stock.yahoo}")
interface YahooFinanceClient {

  @GetMapping("/v8/finance/chart/{code}")
  fun fetchFinanceChart(
    @PathVariable("code") code: String,
    @RequestParam("interval") interval: String = "1d",
    @RequestParam("range") range: String,
  ): YahooFinanceChartResponse
}
