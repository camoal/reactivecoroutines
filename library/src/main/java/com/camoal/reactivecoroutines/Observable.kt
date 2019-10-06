package com.camoal.reactivecoroutines

/**
 * The Observable class offer a factory method to consume synchronous and/or asynchronous reactive dataflows.
 */
abstract class Observable<T> : ObservableSource<T> {

    companion object {

        /**
         * Provide a Observable that that bridges the reactive world with the callback-style world.
         * The Observable allows to execute coroutine methods.
         *
         * @param emitter is called when an Observer subscribes.
         *
         * @return the new Observable instance.
         */
        fun <T> create(emitter: ObservableEmitter<T>): Observable<T> {
            return ObservableCreate(emitter)
        }
    }
}