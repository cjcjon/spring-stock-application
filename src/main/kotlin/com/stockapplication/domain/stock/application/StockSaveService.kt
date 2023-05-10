package com.stockapplication.domain.stock.application

import com.stockapplication.domain.stock.application.model.StockResult
import com.stockapplication.domain.stock.domain.Stock
import com.stockapplication.domain.stock.infrastructure.StockRepository
import com.stockapplication.global.exception.InvalidRequestException
import com.stockapplication.global.utils.RequireUtil.requireOrThrow
import com.stockapplication.infrastructure.finance.FinanceAdaptor
import com.stockapplication.infrastructure.finance.model.StockResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class StockSaveService(
  private val financeAdaptor: FinanceAdaptor,
  private val stockRepository: StockRepository,
) {

  @Transactional
  fun saveDailyStocks(code: String, range: Int): List<StockResult> {
    requireOrThrow(code.isNotBlank() && range > 0) { InvalidRequestException() }

    val fetchedList = fetchStockList(code, range)
    if (fetchedList.isEmpty()) return emptyList()

    val savedList = findStockList(code, fetchedList.first().date, fetchedList.last().date)

    val toSaveStockList = findToSaveStockList(fetchedList, savedList)
    val toUpdateStockList = savedList.map { stock -> updateStock(stock, fetchedList) }

    return stockRepository
      .saveAll(toUpdateStockList + toSaveStockList)
      .map(StockResult::from)
      .sortedBy { it.date }
  }

  private fun fetchStockList(code: String, range: Int): List<StockResponse> =
    financeAdaptor.fetchDailyStock(code, range).sortedBy { it.date }

  private fun findStockList(code: String, startDate: LocalDate, endDate: LocalDate): List<Stock> =
    stockRepository.findAllByCodeAndDateGreaterThanEqualAndDateLessThanEqual(
      code,
      startDate,
      endDate,
    ).sortedBy { it.date }

  private fun findToSaveStockList(
    fetchedList: List<StockResponse>,
    savedList: List<Stock>,
  ): List<Stock> {
    val subtractDates = savedList.map { it.date }

    return fetchedList.filterNot { subtractDates.contains(it.date) }.map(Stock::from)
  }

  private fun updateStock(stock: Stock, fetchedList: List<StockResponse>): Stock =
    fetchedList.find { it.date == stock.date }?.let {
      stock.updateChart(
        high = it.high,
        low = it.low,
        volume = it.volume,
        open = it.open,
        close = it.close,
      )
    } ?: stock
}
