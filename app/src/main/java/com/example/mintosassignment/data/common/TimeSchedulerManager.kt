package com.example.mintosassignment.data.common

import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

object TimeSchedulerManager {

    private lateinit var timer: Timer

    fun startTimer(action: () -> Unit) {
        timer = Timer()
        timer.scheduleAtFixedRate(3000, 3000) {
            action()
        }
    }

    fun stopTimer() {
        timer.cancel()
    }

}