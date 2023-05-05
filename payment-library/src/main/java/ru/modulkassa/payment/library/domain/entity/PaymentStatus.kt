package ru.modulkassa.payment.library.domain.entity

data class PaymentStatus(
    val isSuccess: Boolean,
    val transactionId: String? = null,
)