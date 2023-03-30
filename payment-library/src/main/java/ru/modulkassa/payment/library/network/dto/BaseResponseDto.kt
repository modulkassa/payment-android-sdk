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
     * Ассоциативный список "<Поле>-<Текст ошибки в поле>"
     */
    @SerializedName("field_errors")
    val fieldErrors: Map<String, String>? = null

    /**
     * Если ошибка не связана с каким-либо полем, то сообщение будет в [formErrors]
     * в формате "<Описание ошибки1, Описание ошибки2>"
     */
    @SerializedName("form_errors")
    val formErrors: String? = null

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
