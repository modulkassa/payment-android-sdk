package ru.modulkassa.payment.library.network.mapper

import ru.modulkassa.payment.library.domain.SignatureGenerator
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.network.dto.CreateSbpPaymentRequestDto
import ru.modulkassa.payment.library.network.dto.position.PaymentMethodDto
import ru.modulkassa.payment.library.network.dto.position.PaymentObjectDto
import ru.modulkassa.payment.library.network.dto.position.PositionDto
import ru.modulkassa.payment.library.network.dto.position.TaxationModeDto
import ru.modulkassa.payment.library.network.dto.position.VatTagDto

internal class CreateSbpPaymentRequestMapper(
    private val options: PaymentOptions
) {
    fun toDto(): CreateSbpPaymentRequestDto {
        return CreateSbpPaymentRequestDto(
            merchant = options.merchantId,
            amount = options.amount ?: options.calculateAmount(),
            orderId = options.orderId,
            description = options.description,
            receiptContact = options.receiptContact,
            receiptItems = options.positions?.map {
                positionToDto(it)
            },
            signature = SignatureGenerator.generate(options.signatureKey)
        )
    }

    private fun positionToDto(position: Position): PositionDto {
        return PositionDto(
            name = position.name,
            quantity = position.quantity,
            price = position.price,
            taxationMode = try {
                TaxationModeDto.valueOf(position.taxationMode.name)
            } catch (e: IllegalArgumentException) {
                // todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
                throw Throwable("Кривое СНО")
            },
            paymentObject = try {
                PaymentObjectDto.valueOf(position.paymentObject.name)
            } catch (e: IllegalArgumentException) {
                // todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
                throw Throwable()
            },
            paymentMethod = try {
                PaymentMethodDto.valueOf(position.paymentMethod.name)
            } catch (e: IllegalArgumentException) {
                // todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
                throw Throwable()
            },
            vat = try {
                VatTagDto.valueOf(position.vat.name)
            } catch (e: IllegalArgumentException) {
                // todo SDK-9 Продумать обработку ошибок из апи и rx цепочек
                throw Throwable()
            }
        )
    }
}