package ru.modulkassa.payment.library.domain

internal object SignatureGenerator {
    fun generate(secretKey: String): String {
        // todo SDK-11 Разобраться с генерацией подписи
        return secretKey
    }
}