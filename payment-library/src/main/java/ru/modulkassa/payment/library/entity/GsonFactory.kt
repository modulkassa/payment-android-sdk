package ru.modulkassa.payment.library.entity

import com.google.gson.Gson
import com.google.gson.GsonBuilder

internal class GsonFactory private constructor() {
    companion object {
        fun provide(): Gson {
            return GsonBuilder()
                .create()
        }
    }
}