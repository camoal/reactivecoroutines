package com.camoal.reactivecoroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import kotlin.coroutines.CoroutineContext


/**
 * The ObservableCreate class offer differents methods to take the control of the reactive events.
 *
 * @param emitter
 */
class ObservableCreate<T>(private val emitter: ObservableEmitter<T>) : Observable<T>(), Emitter<T>, Disposable {

    override val coroutineContext: CoroutineContext get() = Dispatchers.IO
    private var observerDispatcher: CoroutineContext = Dispatchers.Main
    private var subscriberDispatcher: CoroutineContext = coroutineContext

    private var job: Job? = null
    private var retryTimes = 0
    private var repeatDelay: Long = 0
    private var repeat = false
    private var test = false

    private var doOnSubscribeFunction: CompleteListener = {}
    private var doOnTerminateFunction: CompleteListener = {}
    private var retryWhenFuction: Predicate<Throwable>? = null

    private lateinit var onNextListener: NextListener<T>
    private lateinit var onErrorListener: ErrorListener
    private lateinit var onCompleteListener: CompleteListener

    override fun test(): TestObservableSource<T> {

        test = true
        repeat = false
        retryTimes = 0
        retryWhenFuction = null
        return TestObservable(this)
    }

    override fun observeOn(dispatcher: CoroutineContext) = apply {

        observerDispatcher = dispatcher
    }

    override fun subscribeOn(dispatcher: CoroutineContext) = apply {

        subscriberDispatcher = dispatcher
    }

    override fun doOnSubscribe(function: SubscribeListener) = apply {

        doOnSubscribeFunction = function
    }

    override fun doOnTerminate(function: TerminateListener) = apply {

        doOnTerminateFunction = function
    }

    override fun retry(times: Int) = apply {

        if(times > 0) {
            retryTimes = times
        }
    }

    override fun retryWhen(predicate: Predicate<Throwable>) = apply {

        if(retryTimes == 0){
            retryTimes = 1
        }
        retryWhenFuction = predicate
    }

    override fun repeat(delayInMilliseconds: Long) = apply {

        if(delayInMilliseconds > 0) {
            repeat = true
            repeatDelay = delayInMilliseconds
        }
    }

    override fun subscribe(onNext: NextListener<T>,
                           onError: ErrorListener,
                           onComplete: CompleteListener): Disposable = apply {

        this.onNextListener = onNext
        this.onErrorListener = onError
        this.onCompleteListener = onComplete

        execute()
    }

    override suspend fun onNext(value: T) {

        sendEvent(false) { onNextListener(value) }
    }

    override suspend fun onError(error: Throwable) {

        repeat = false
        sendEvent { onErrorListener(error) }
    }

    override suspend fun onComplete() {

        sendEvent { onCompleteListener() }
    }

    override fun dispose() {

        launch {
            job?.cancelAndJoin()
        }
    }

    override fun isDisposed(): Boolean {

        return job?.isCancelled ?: true
    }

    /**
     * Execute the observable tasks.
     *
     * @param requireRepeat default is false.
     */
    private fun execute(requireRepeat: Boolean = false){

        job = launch(subscriberDispatcher) {
            try {
                if(requireRepeat){
                    delay(repeatDelay)
                }
                withContext(observerDispatcher) { doOnSubscribeFunction() }
                when(test){
                    true -> runBlockingTest { emitter() }
                    false -> emitter()
                }
            }
            catch (t: Throwable){
                if(t !is CancellationException){
                    errorHandler(t)
                }
            }
        }
    }

    /**
     * Handles the errors of the observable. Retry the task if necessary.
     *
     * @see ObservableCreate.retry
     * @see ObservableCreate.retryWhen
     */
    private suspend fun errorHandler(t: Throwable) {

        if (retryTimes > 0){
            retryTimes--
            retryWhenFuction?.let {function ->
                if(function(t)){
                    execute()
                }
                else this@ObservableCreate.onError(t)
            } ?: execute()
        }
        else this@ObservableCreate.onError(t)
    }

    /**
     * If the coroutine is active, the event is emitted.
     * Repeat the observable if necessary or finish the task.
     *
     * @param terminate indicates if the terminate task is executed. Default is true.
     * @param function indicates the function that is executed.
     * @see ObservableCreate.onNext
     * @see ObservableCreate.onError
     * @see ObservableCreate.onComplete
     */
    private fun sendEvent(terminate: Boolean = true, function: CompleteListener){

        if(job?.isActive == true){
            launch(observerDispatcher) {
                function()
                if(terminate){
                    doOnTerminateFunction()
                }
            }
            if(terminate) {
                if(repeat) {
                    execute(true)
                }
                else{
                    dispose()
                }
            }
        }
    }
}