package ru.modulkassa.payment.library

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Switching all schedulers to {@see Schedulers.trampoline()} for testing
 */
class RxRule : TestRule {

    var schedulerForTest: Scheduler
        private set
    var taskSchedulerForTest: Scheduler
        private set

    constructor() {
        schedulerForTest = Schedulers.trampoline()
        taskSchedulerForTest = schedulerForTest
    }

    constructor(scheduler: Scheduler = Schedulers.trampoline(), taskScheduler: Scheduler? = null) {
        this.schedulerForTest = scheduler
        taskSchedulerForTest = taskScheduler ?: scheduler
    }

    override fun apply(base: Statement, description: Description): Statement {
        return statement(base)
    }

    protected fun after() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
        RxPlugins.reset()
    }

    protected fun before() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { schedulerForTest }
        RxAndroidPlugins.setMainThreadSchedulerHandler { schedulerForTest }
        RxJavaPlugins.setIoSchedulerHandler { schedulerForTest }
        RxJavaPlugins.setNewThreadSchedulerHandler { schedulerForTest }
        RxJavaPlugins.setComputationSchedulerHandler { schedulerForTest }
        RxPlugins.setTaskSchedulerHandler { taskSchedulerForTest }
    }

    private fun statement(base: Statement): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                before()
                try {
                    base.evaluate()
                } finally {
                    after()
                }
            }
        }
    }
}

/**
 * Вспомогательный класс для замены обработчика Rx операций
 */
object RxPlugins {

    private var onTaskSchedulerHandler: ((Scheduler) -> Scheduler)? = null

    fun setTaskSchedulerHandler(handler: ((Scheduler) -> Scheduler)?) {
        onTaskSchedulerHandler = handler
    }

    fun onTaskScheduler(scheduler: Scheduler): Scheduler {
        try {
            return onTaskSchedulerHandler?.invoke(scheduler) ?: scheduler
        } catch (throwable: Throwable) {
            throw Exceptions.propagate(throwable)
        }
    }

    fun reset() {
        onTaskSchedulerHandler = null
    }

}