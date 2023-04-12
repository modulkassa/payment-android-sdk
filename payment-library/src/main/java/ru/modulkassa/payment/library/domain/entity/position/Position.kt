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
     * Предмет расчета
     */
    val type: PositionType,
    /**
     * Система налоообложения
     */
    val taxationMode: TaxationMode = TaxationMode.OSN,
    /**
     * Метод платежа
     */
    val paymentType: PaymentType = PaymentType.FULL_PAYMENT,
    /**
     * Ставка НДС
     */
    val vat: VatTag = VatTag.VAT_20
)