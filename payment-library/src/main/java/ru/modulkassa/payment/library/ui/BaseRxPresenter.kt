package ru.modulkassa.payment.library.ui

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

internal interface BaseView

internal interface BaseUserActions<View : BaseView> {

    /**
     * Привязать view
     */
    fun attachView(view: View?)

    /**
     * Отвязать view
     */
    fun detachView()
}

internal open class BaseRxPresenter<View : BaseView> : BaseUserActions<View> {

    private val unsubscribeOnDestroyDisposables = CompositeDisposable()
    private var baseView: View? = null

    fun unsubscribeOnDestroy(disposable: Disposable, vararg disposables: Disposable) {
        unsubscribeOnDestroyDisposables.add(disposable)

        for (d in disposables) {
            unsubscribeOnDestroyDisposables.add(d)
        }
    }

    /**
     * Получить привязанную view
     */
    fun getView(): View? {
        return baseView
    }

    override fun attachView(view: View?) {
        baseView = view
    }

    override fun detachView() {
        baseView = null
        unsubscribeOnDestroyDisposables.clear()
    }
}