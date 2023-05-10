package com.stockapplication.domain.stock.infrastructure

import com.stockapplication.domain.stock.domain.Stock
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface StockRepository : JpaRepository<Stock, Long> {

  fun findAllByCodeAndDateGreaterThanEqualAndDateLessThanEqual(
    code: String,
    gteDate: LocalDate,
    lteDate: LocalDate,
  ): List<Stock>
}
