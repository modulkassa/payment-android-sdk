package ru.modulkassa.payment.library

internal interface SettingsRepository {

    /**
     * Сохранить данные мерчанта и ключа подписи
     */
    fun save(merchantId: String, signatureKey: String)

    /**
     * Получить идентификатор мерчанта
     */
    fun getMerchantId(): String?

    /**
     * Получить ключ подписи
     */
    fun getSignatureKey(): String?
}