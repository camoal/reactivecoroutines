package com.camoal.reactivecoroutines.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        countdownButton.setOnClickListener {
            mainViewModel.startCountdown()
        }

        mainViewModel.countdownState.observe(this, Observer { state ->
            when(state){
                is CountdownState.Start -> {
                    countdownButton.visibility = View.GONE
                    countdownText.text = getString(R.string.countdown_start)
                }
                is CountdownState.End -> {
                    countdownButton.visibility = View.VISIBLE
                    countdownText.text = getString(R.string.countdown_end)
                }
                is CountdownState.Count -> countdownText.text = state.count.toString()
                is CountdownState.Error -> countdownText.text = state.message
            }
        })
    }
}
