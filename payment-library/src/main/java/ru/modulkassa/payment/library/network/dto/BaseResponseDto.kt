package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName

internal abstract class BaseResponseDto {
    /**
     * Статус ответа
     * "error" - ошибочный,
     * "ok" - упешный
     */
    val status: BaseResponseStatus? = null
}

internal enum class BaseResponseStatus {
    @SerializedName("ok")
    OK,

    @SerializedName("error")
    ERROR
}
