package com.stockapplication.domain.stock.presentation

import com.stockapplication.domain.stock.application.StockSaveService
import com.stockapplication.domain.stock.application.model.StockResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stocks")
class StockController(private val saveService: StockSaveService) {

  @PostMapping("/samsung")
  fun saveSamsungStockChart(): ResponseEntity<List<StockResult>> {
    val result = saveService.saveDailyStocks(SAMSUNG_CODE, 5)

    return ResponseEntity(result, HttpStatus.OK)
  }

  companion object {
    private const val SAMSUNG_CODE = "005930.KS"
  }
}
