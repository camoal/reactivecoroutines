package com.camoal.reactivecoroutines

import kotlin.coroutines.CoroutineContext

interface ObservableSource<T> {

    /**
     * Provide a TestObservable and cancel the repeat and retry options.
     *
     * @return TestObservableSource.
     */
    fun test(): TestObservableSource<T>

    /**
     * Specify the dispatcher where the observable receive the events emitted.
     *
     * @return ObservableSource.
     */
    fun observeOn(dispatcher: CoroutineContext): ObservableSource<T>

    /**
     * Specify the dispatcher where the observable is executed.
     *
     * @return ObservableSource.
     */
    fun subscribeOn(dispatcher: CoroutineContext): ObservableSource<T>

    /**
     * Specify the task that is executed when you subscribe to the observable.
     *
     * @param function is executed when you subscribe to the observable.
     * @return ObservableSource.
     */
    fun doOnSubscribe(function: SubscribeListener): ObservableSource<T>

    /**
     * Specify the task that is executed when the observable ends.
     *
     * @param function is executed when the observable ends.
     * @return ObservableSource.
     */
    fun doOnTerminate(function: TerminateListener): ObservableSource<T>

    /**
     * Specify the number of times the task is repeated.
     *
     * @param times number of times the task is repeated.
     * @return ObservableSource.
     */
    fun retry(times: Int): ObservableSource<T>

    /**
     * In case of error the observable retry when the predicate is fulfilled.
     *
     * @param predicate
     * @return ObservableSource.
     */
    fun retryWhen(predicate: Predicate<Throwable>): ObservableSource<T>

    /**
     * Specify time in milliseconds to rerun the task.
     *
     * @param delayInMilliseconds time in milliseconds to rerun the task.
     * @return ObservableSource.
     */
    fun repeat(delayInMilliseconds: Long): ObservableSource<T>

    /**
     * Subscribes to an ObservableSource and provides callbacks to handle the items it emits
     * and any error or completion notification.
     *
     * @param onNext accepts emissions from an ObservableSource.
     * @param onError accepts any error from an ObservableSource.
     * @param onComplete accepts the completion notification from an ObservableSource.
     * @return disposable.
     * @see Disposable
     */
    fun subscribe(onNext: NextListener<T> = {}, onError: ErrorListener = {}, onComplete: CompleteListener = {}): Disposable
}