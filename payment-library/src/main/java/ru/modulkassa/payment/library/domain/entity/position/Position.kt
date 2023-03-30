package ru.modulkassa.payment.library.domain.entity.position

import java.math.BigDecimal

/**
 * Позиция платежа
 */
data class Position(
    /**
     * Наименование товара/услуги
     */
    var name: String,
    /**
     * Цена товара
     * Максимум 8 символов до запятой и 2 символа после запятой
     */
    var price: BigDecimal,
    /**
     * Количество товара
     * Максимум 5 символов до запятой и 3 символа после запятой
     */
    var quantity: BigDecimal,
    /**
     * Система налоообложения
     */
    val taxationMode: TaxationMode,
    /**
     * Предмет расчета
     */
    val paymentObject: PaymentObject,
    /**
     * Метод платежа
     */
    val paymentMethod: PaymentMethod,
    /**
     * Ставка НДС
     */
    val vat: VatTag
)