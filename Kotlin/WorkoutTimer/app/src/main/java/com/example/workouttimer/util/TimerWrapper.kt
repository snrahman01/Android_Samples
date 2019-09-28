package com.example.workouttimer.util

import java.util.*

interface Timer {
    fun reset()
    fun start(task: TimerTask)
    fun getElapsedTime(): Long
    fun updatePausedTime()
    fun getPausedTime() : Long
    fun resetStartTime()
    fun resetPauseTime()
}

object DefaultTimer : Timer {

    private const val TIMER_PERIOD_MS = 100L

    private var startTime = System.currentTimeMillis()
    private var pauseTime = 0L

    override fun getPausedTime() : Long = pauseTime - startTime

    override fun getElapsedTime() = System.currentTimeMillis() - startTime

    override fun resetPauseTime() {
        pauseTime = System.currentTimeMillis()
    }

    override fun resetStartTime() {
        startTime = System.currentTimeMillis()
    }

    override fun updatePausedTime() {
        startTime += System.currentTimeMillis() - pauseTime
    }

    private var timer = java.util.Timer()

    override fun reset() {
        timer.cancel()
    }

    override fun start(task: TimerTask) {
        timer = java.util.Timer()
        timer.scheduleAtFixedRate(task, 0, TIMER_PERIOD_MS)
    }
}