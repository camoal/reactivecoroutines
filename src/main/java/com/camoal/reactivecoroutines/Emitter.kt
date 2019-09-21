package com.camoal.reactivecoroutines

import kotlinx.coroutines.CoroutineScope

interface Emitter<T>: CoroutineScope {

    /**
     * Method that allows to emit a new element to an ObservableSource.
     *
     * @param value that is emits to an ObservableSource.
     */
    suspend fun onNext(value: T)

    /**
     * Method that allows to emit an error to an ObservableSource.
     *
     * @param error that is emits to an ObservableSource.
     */
    suspend fun onError(error: Throwable)

    /**
     * Method that allows to emit a completion notification from an ObservableSource.
     */
    suspend fun onComplete()
}
