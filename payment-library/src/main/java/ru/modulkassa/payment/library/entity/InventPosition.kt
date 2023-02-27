package ru.modulkassa.payment.library.entity

import java.math.BigDecimal

/**
 * Позиция платежа
 */
data class InventPosition(
    /**
     * Наименование товара/услуги
     */
    var name: String,
    /**
     * Цена товара
     * Точность должна быть указана до 2х знаков [BigDecimal.setScale(2, BigDecimal.ROUND_DOWN)]
     */
    var price: BigDecimal,
    /**
     * Количество товара
     * Точность должна быть указана до 3х знаков [BigDecimal.setScale(3, BigDecimal.ROUND_DOWN)]
     */
    var quantity: BigDecimal
)