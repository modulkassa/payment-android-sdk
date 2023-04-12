package ru.modulkassa.payment.library.network.mapper

import com.google.gson.Gson
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.network.BigDecimalFormatter
import ru.modulkassa.payment.library.network.SignatureGenerator
import ru.modulkassa.payment.library.network.dto.CreateSbpPaymentRequestDto
import ru.modulkassa.payment.library.network.dto.position.PaymentMethodDto
import ru.modulkassa.payment.library.network.dto.position.PaymentObjectDto
import ru.modulkassa.payment.library.network.dto.position.PositionDto
import ru.modulkassa.payment.library.network.dto.position.TaxationModeDto
import ru.modulkassa.payment.library.network.dto.position.VatTagDto
import ru.modulkassa.payment.library.ui.ValidationException

internal class CreateSbpPaymentRequestMapper(
    private val gson: Gson
) {
    fun toDto(options: PaymentOptions): CreateSbpPaymentRequestDto {
        val requestDto = CreateSbpPaymentRequestDto(
            merchant = options.merchantId,
            amount = BigDecimalFormatter.format(options.amount ?: options.calculateAmount()),
            orderId = options.orderId,
            description = fixLineBreaks(options.description),
            receiptContact = options.receiptContact,
            receiptItems = options.positions?.let { positions ->
                gson.toJson(positions.map { positionToDto(it) })
            }
        )
        val generatedSignature = SignatureGenerator(gson).generate(requestDto, options.signatureKey)
        requestDto.signature = generatedSignature
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