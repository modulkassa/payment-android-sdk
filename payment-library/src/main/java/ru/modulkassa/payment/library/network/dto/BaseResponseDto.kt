package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName

internal abstract class BaseResponseDto {
    /**
     * Статус ответа
     * "error" - ошибочный,
     * "ok" - упешный
     */
    val status: BaseResponseStatus? = null
    /**
     * Общий текст ошибки по всему платежу
     * Конкатенация из [fieldErrors] по "<Именование поля>:<Текст ошибки>;", если неверно заполнены поля
     * по формату "<Не заполнены поля>: *, *, *;", если пустые поля
     */
    val message: String? = null
}

internal enum class BaseResponseStatus {
    @SerializedName("ok")
    OK,

    @SerializedName("error")
    ERROR
}
