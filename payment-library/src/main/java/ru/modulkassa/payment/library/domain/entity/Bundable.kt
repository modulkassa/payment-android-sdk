package ru.modulkassa.payment.library.domain.entity

import android.os.Bundle

/**
 * Сущность поддерживает сохранение в Bundle
 */
internal interface Bundable {
    fun toBundle(): Bundle
}