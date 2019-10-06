package com.camoal.reactivecoroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.util.concurrent.CountDownLatch

/**
 * The TestObservable class offers the methods to perform unit and instrumented tests.
 *
 * @param observable
 */
class TestObservable<T>(private val observable: ObservableSource<T>): TestObservableSource<T> {

    override fun assert(vararg values: T) {

        val singleThreadContext = newSingleThreadContext("UI thread")
        Dispatchers.setMain(singleThreadContext)

        val latch = CountDownLatch(1)
        var error: Throwable? = null

        val list = values.toMutableList()

        observable
            .subscribe(
                { value ->
                    if(list[0] != value){
                        error = AssertionError()
                    }
                    list.removeAt(0)
                },
                { throwable ->
                    error = throwable
                    latch.countDown()
                },
                {
                    if(list.size != 0){
                        error = AssertionError()
                    }
                    latch.countDown()
                })

        latch.await()
        Dispatchers.resetMain()
        singleThreadContext.close()
        error?.let { throwable ->
            throw throwable
        }
    }
}