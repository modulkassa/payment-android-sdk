package ru.modulkassa.payment.library.network.dto.position

import com.google.gson.annotations.SerializedName

/**
 * Позиция оплаты для фискализации чека
 */
internal data class PositionDto(
    /**
     * Наименование товара
     */
    val name: String,
    /**
     * Количество
     */
    val quantity: String,
    /**
     * Цена за единицу товара
     */
    val price: String,
    /**
     * Система налоообложения
     */
    @SerializedName("sno")
    val taxationMode: TaxationModeDto,
    /**
     * Предмет расчета
     */
    @SerializedName("payment_object")
    val paymentObject: PaymentObjectDto,
    /**
     * Метод платежа
     */
    @SerializedName("payment_method")
    val paymentMethod: PaymentMethodDto,
    /**
     * Ставка НДС
     */
    val vat: VatTagDto
)