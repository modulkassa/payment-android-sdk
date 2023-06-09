package ru.modulkassa.payment.library.network.dto

import com.google.gson.annotations.SerializedName

internal data class SbpPaymentLinkResponseDto(
    @SerializedName("sbp_link")
    val sbpLink: String
) : BaseResponseDto()