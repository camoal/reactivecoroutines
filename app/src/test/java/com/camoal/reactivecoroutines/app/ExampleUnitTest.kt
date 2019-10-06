package com.camoal.reactivecoroutines.app

import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp(){
        viewModel = MainViewModel()
    }

    @Test
    fun failsIfTheValueDoesNotStartAtTenAndEndsAtZero() {
        val list = mutableListOf<Int>()
        for(i in 10 downTo 0){
            list.add(i)
        }
        viewModel
            .countdownObservable()
            .test()
            .assert(list)
    }
}
