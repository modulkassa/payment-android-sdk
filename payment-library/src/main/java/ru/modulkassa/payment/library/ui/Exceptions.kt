package ru.modulkassa.payment.library.ui

/**
 * Не указаны параметры оплаты
 */
internal class NoPaymentOptionsException : Exception()

/**
 * Отменено пользователем
 */
internal class CanceledByUserException : Exception()