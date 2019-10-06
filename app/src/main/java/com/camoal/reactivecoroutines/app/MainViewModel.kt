package com.camoal.reactivecoroutines.app

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.camoal.reactivecoroutines.Disposable
import com.camoal.reactivecoroutines.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class MainViewModel: ViewModel() {

    var countdownState = MutableLiveData<CountdownState>()
    private val disposables = mutableListOf<Disposable>()

    fun startCountdown(){

        disposables
            .add(countdownObservable()
            .observeOn(Dispatchers.Main)
            .subscribeOn(Dispatchers.IO)
            .doOnSubscribe { countdownState.value = CountdownState.Start() }
            .doOnTerminate { countdownState.value = CountdownState.End() }
            .subscribe(
                { value ->
                    countdownState.value = CountdownState.Count(value)
                },
                { throwable ->
                    countdownState.value = CountdownState.Error(throwable.message)
                },
                {
                    Log.v("Countdown", "Complete")
                }
            ))
    }

    fun countdownObservable(): Observable<Int> {

        return Observable.create {
            for(i in 10 downTo 0){
                delay(1000)
                this.onNext(i)
            }
            this.onComplete()
        }
    }

    override fun onCleared() {
        super.onCleared()

        disposables.forEach { disposable ->
            disposable.dispose()
        }
    }
}