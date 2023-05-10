package com.stockapplication.domain.stock.presentation

import com.stockapplication.domain.stock.application.StockSaveService
import com.stockapplication.domain.stock.application.model.StockResult
import com.stockapplication.global.exception.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "주식 저장", description = "주식 정보를 저장하기 위한 API 입니다")
@RestController
@RequestMapping("/api/v1/stocks")
class StockController(private val saveService: StockSaveService) {

  @Operation(
    summary = "삼성전자 주식 데이터 저장",
    description = "삼성 주식의 최근 5일치 정보를 저장 후 출력합니다\n데이터는 5개 이하일 수 있습니다.",
  )
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "저장 성공",
        content = [
          Content(
            mediaType = "application/json",
            array = ArraySchema(schema = Schema(implementation = StockResult::class), maxItems = 5),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "500",
        description = "서버 에러",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @PostMapping("/samsung")
  fun saveSamsungStockChart(): ResponseEntity<List<StockResult>> {
    val result = saveService.saveDailyStocks(SAMSUNG_CODE, 5)

    return ResponseEntity(result, HttpStatus.OK)
  }

  companion object {
    private const val SAMSUNG_CODE = "005930.KS"
  }
}
