package ru.modulkassa.payment.library.network.dto.position

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

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
    val quantity: BigDecimal,
    /**
     * Цена за единицу товара
     */
    val price: BigDecimal,
    /**
     * Система налоообложения
     */
    @SerializedName("osn")
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