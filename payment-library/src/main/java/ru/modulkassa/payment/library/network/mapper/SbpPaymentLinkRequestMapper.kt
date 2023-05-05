package ru.modulkassa.payment.library.network.mapper

import com.google.gson.Gson
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.SettingsRepository
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.network.BigDecimalFormatter
import ru.modulkassa.payment.library.network.dto.SbpPaymentLinkRequestDto
import ru.modulkassa.payment.library.network.dto.position.PaymentMethodDto
import ru.modulkassa.payment.library.network.dto.position.PaymentObjectDto
import ru.modulkassa.payment.library.network.dto.position.PositionDto
import ru.modulkassa.payment.library.network.dto.position.TaxationModeDto
import ru.modulkassa.payment.library.network.dto.position.VatTagDto
import ru.modulkassa.payment.library.ui.ValidationException

internal class SbpPaymentLinkRequestMapper(
    private val gson: Gson,
    private val repository: SettingsRepository
) {
    fun toDto(options: PaymentOptions): SbpPaymentLinkRequestDto {
        val requestDto = SbpPaymentLinkRequestDto(
            merchant = repository.getMerchantId()
                ?: throw ValidationException(causeResource = R.string.error_validation_no_merchant_id),
            amount = BigDecimalFormatter.format(options.amount ?: options.calculateAmount()),
            orderId = options.orderId,
            description = fixLineBreaks(options.description),
            receiptContact = options.receiptContact,
            receiptItems = options.positions?.let { positions ->
                gson.toJson(positions.map { positionToDto(it) })
            }
        )
        return requestDto
    }

    private fun fixLineBreaks(source: String): String {
        return source.replace("\r", "")
            .replace("\n", "\r\n")
    }

    private fun positionToDto(position: Position): PositionDto {
        return PositionDto(
            name = position.name,
            quantity = BigDecimalFormatter.format(position.quantity, scale = 3),
            price = BigDecimalFormatter.format(position.price),
            taxationMode = try {
                TaxationModeDto.valueOf(position.taxationMode.name)
            } catch (e: IllegalArgumentException) {
                throw ValidationException(causeResource = R.string.error_validation_incorrect_taxation_mode)
            },
            paymentObject = try {
                PaymentObjectDto.valueOf(position.type.name)
            } catch (e: IllegalArgumentException) {
                throw ValidationException(causeResource = R.string.error_validation_incorrect_payment_object)
            },
            paymentMethod = try {
                PaymentMethodDto.valueOf(position.paymentType.name)
            } catch (e: IllegalArgumentException) {
                throw ValidationException(causeResource = R.string.error_validation_incorrect_payment_method)
            },
            vat = try {
                VatTagDto.valueOf(position.vat.name)
            } catch (e: IllegalArgumentException) {
                throw ValidationException(causeResource = R.string.error_validation_incorrect_vat_tag)
            }
        )
    }
}