package com.stockapplication.global.extension

fun <T> List<T>.zipListAll(vararg others: List<T>): List<List<T>> {
  val startZip =
    this.zip(others.firstOrNull() ?: emptyList()) { first, second -> listOf(first, second) }

  return others.drop(1).fold(startZip) { acc, listValue ->
    acc.zip(listValue) { prev, value -> prev + value }
  }
}

