package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName
import java.util.UUID

abstract class BaseRequestDto {
    /**
     * Криптографическая подпись
     */
    var signature: String? = null

    /**
     * Текущее дата и время
     */
    @SerializedName("unix_timestamp")
    val unixTimestamp: String = (System.currentTimeMillis() / 1000).toString()

    /**
     * Случайная величина
     * Строка (максимум 32 символа), допускаются только печатные ASCII символы
     */
    val salt: String = UUID.randomUUID().toString().take(32)
}