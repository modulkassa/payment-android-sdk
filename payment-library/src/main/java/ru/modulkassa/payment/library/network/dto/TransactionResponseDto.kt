package ru.modulkassa.payment.library.network.dto

internal data class TransactionResponseDto(
    val transaction: TransactionDto
) : BaseResponseDto()
