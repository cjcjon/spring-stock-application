package com.stockapplication.infrastructure.finance

import com.stockapplication.infrastructure.finance.model.StockResponse

interface FinanceAdaptor {
  fun fetchDailyStock(code: String, range: Int): List<StockResponse>
}
