package ru.modulkassa.payment.library.entity

import android.os.Bundle

/**
 * Сущность поддерживает сохранение в Bundle
 */
internal interface Bundable {
    fun toBundle(): Bundle
}