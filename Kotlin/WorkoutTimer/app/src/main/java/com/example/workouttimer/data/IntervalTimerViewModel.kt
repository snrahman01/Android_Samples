package com.example.workouttimer.data

import androidx.databinding.Bindable
import androidx.databinding.ObservableInt
import androidx.databinding.library.baseAdapters.BR
import com.example.workouttimer.util.ObservableViewModel
import com.example.workouttimer.util.Timer
import com.example.workouttimer.util.cleanSecondsString
import java.util.*
import kotlin.math.round

const val INITIAL_SECONDS_PER_WORK_SET = 5
const val INITIAL_SECONDS_PER_REST_SET = 2
const val INITIAL_NUMBER_OF_SETS = 5

class IntervalTimerViewModel(private val timer: Timer) : ObservableViewModel() {

    val timePerWorkSet = ObservableInt(INITIAL_SECONDS_PER_WORK_SET * 10)
    val timePerRestSet = ObservableInt(INITIAL_SECONDS_PER_REST_SET * 10)
    val workTimeLeft = ObservableInt(timePerWorkSet.get())
    val restTimeLeft = ObservableInt(timePerRestSet.get())


    private var state = TimerStates.STOPPED
    private var stage = StartedStages.WORKING
    private var numberOfSetsTotal = INITIAL_NUMBER_OF_SETS
    private var numberOfSetsElapsed = 0

    var timerRunning: Boolean
        @Bindable get() {
            return state == TimerStates.STARTED
        }
        set(value) {
            if (value) startButtonClicked() else pauseButtonClicked()
        }

    var numberOfSets: Array<Int> = emptyArray()
        @Bindable get() {
            return arrayOf(numberOfSetsElapsed, numberOfSetsTotal)
        }
        set(value: Array<Int>) {
            val newTotal = value[1]
            if (newTotal == numberOfSets[1]) return
            if (newTotal != 0 && newTotal > numberOfSetsElapsed) {
                field = value
                numberOfSetsTotal = newTotal
            }
            notifyPropertyChanged(BR.numberOfSets)
        }

    val inWorkingStage: Boolean
        @Bindable get() {
            return stage == StartedStages.WORKING
        }


    fun timePerWorkSetChanged(newValue: CharSequence) {
        try {
            timePerWorkSet.set(cleanSecondsString(newValue.toString()))
        } catch (e: NumberFormatException) {
            return
        }
        if (!timerRunning) {
            workTimeLeft.set(timePerWorkSet.get())
        }
    }

    fun timePerRestSetChanged(newValue: CharSequence) {
        try {
            timePerRestSet.set(cleanSecondsString(newValue.toString()))
        } catch (e: NumberFormatException) {
            return
        }
        if (!isRestTimeAndRunning()) {
            restTimeLeft.set(timePerRestSet.get())
        }
    }


    fun stopButtonClicked() {
        resetTimers()
        numberOfSetsElapsed = 0
        state = TimerStates.STOPPED
        stage = StartedStages.WORKING // Reset for the next set
        timer.reset()

        notifyPropertyChanged(BR.timerRunning)
        notifyPropertyChanged(BR.inWorkingStage)
        notifyPropertyChanged(BR.numberOfSets)
    }

    private fun startButtonClicked() {
        when(state) {
            TimerStates.PAUSED -> {
                pausedToStarted()
            }
            TimerStates.STOPPED -> {
                stoppedToStarted()
            }
            TimerStates.STARTED -> {
            }
        }

        val task = object : TimerTask() {
            override fun run() {
                if (state == TimerStates.STARTED) {
                    updateCountdowns()
                }
            }
        }

        timer.start(task)
    }

    private fun pauseButtonClicked() {

        if (state == TimerStates.STARTED) {
            startedToPaused()
        }
        notifyPropertyChanged(BR.timerRunning)
    }


    fun workTimeIncrease() = timePerSetIncrease(timePerWorkSet, 1)

    fun workTimeDecrease() = timePerSetIncrease(timePerWorkSet, -1, 10)


    fun restTimeIncrease() = timePerSetIncrease(timePerRestSet, 1)

    fun restTimeDecrease() = timePerSetIncrease(timePerRestSet, -1)


    fun setsIncrease() {
        numberOfSetsTotal += 1
        notifyPropertyChanged(BR.numberOfSets)
    }

    fun setsDecrease() { if (numberOfSetsTotal > numberOfSetsElapsed + 1) {
        numberOfSetsTotal -= 1
        notifyPropertyChanged(BR.numberOfSets)
    }
    }



    private fun stoppedToStarted() {
        // Set the start time
        timer.resetStartTime()
        state = TimerStates.STARTED
        stage = StartedStages.WORKING

        notifyPropertyChanged(BR.inWorkingStage)
        notifyPropertyChanged(BR.timerRunning)
    }

    private fun startedToPaused() {
        state = TimerStates.PAUSED
        timer.resetPauseTime()
    }

    private fun pausedToStarted() {
        timer.updatePausedTime()
        state = TimerStates.STARTED
        notifyPropertyChanged(BR.timerRunning)
    }


    private fun resetTimers() {
        workTimeLeft.set(timePerWorkSet.get())
        restTimeLeft.set(timePerRestSet.get())
    }

    private fun isRestTimeAndRunning(): Boolean {
        return (state == TimerStates.PAUSED || state == TimerStates.STARTED)
                && workTimeLeft.get() == 0
    }

    private fun timePerSetIncrease(timePerSet: ObservableInt, sign: Int = 1, min: Int = 0) {
        if (timePerSet.get() < 10 && sign < 0) return
        roundTimeIncrease(timePerSet, sign, min)
        if (state == TimerStates.STOPPED) {
            workTimeLeft.set(timePerWorkSet.get())
            restTimeLeft.set(timePerRestSet.get())
        } else {
            updateCountdowns()
        }
    }

    private fun roundTimeIncrease(timePerSet: ObservableInt, sign: Int, min: Int) {
        val currentValue = timePerSet.get()
        val newValue =
            when {
                currentValue < 100 -> timePerSet.get() + sign * 10
                currentValue < 600 -> (round(currentValue / 50.0) * 50 + (50 * sign)).toInt()
                else -> (round(currentValue / 100.0) * 100 + (100 * sign)).toInt()
            }
        timePerSet.set(newValue.coerceAtLeast(min))
    }


    private fun updateCountdowns() {
        if (state == TimerStates.STOPPED) {
            resetTimers()
            return
        }

        val elapsed = if (state == TimerStates.PAUSED) {
            timer.getPausedTime()
        } else {
            timer.getElapsedTime()
        }

        if (stage == StartedStages.RESTING) {
            updateRestCountdowns(elapsed)
        } else {
            updateWorkCountdowns(elapsed)
        }
    }

    private fun updateWorkCountdowns(elapsed: Long) {
        stage = StartedStages.WORKING
        val newTimeLeft = timePerWorkSet.get() - (elapsed / 100).toInt()
        if (newTimeLeft <= 0) {
            workoutFinished()
        }
        workTimeLeft.set(newTimeLeft.coerceAtLeast(0))
    }

    private fun updateRestCountdowns(elapsed: Long) {
        // Calculate the countdown time with the start time
        val newRestTimeLeft = timePerRestSet.get() - (elapsed / 100).toInt()
        restTimeLeft.set(newRestTimeLeft.coerceAtLeast(0))

        if (newRestTimeLeft <= 0) { // Rest time is up
            numberOfSetsElapsed += 1
            resetTimers()
            if (numberOfSetsElapsed >= numberOfSetsTotal) { // End
                timerFinished()
            } else { // End of set
                setFinished()
            }
        }
    }


    private fun workoutFinished() {
        timer.resetStartTime()
        stage = StartedStages.RESTING
        notifyPropertyChanged(BR.inWorkingStage)
    }

    private fun setFinished() {
        timer.resetStartTime()
        stage = StartedStages.WORKING

        notifyPropertyChanged(BR.inWorkingStage)
        notifyPropertyChanged(BR.numberOfSets)
    }

    private fun timerFinished() {
        state = TimerStates.STOPPED
        stage = StartedStages.WORKING // Reset for the next set
        timer.reset()
        notifyPropertyChanged(BR.timerRunning)
        numberOfSetsElapsed = 0

        notifyPropertyChanged(BR.inWorkingStage)
        notifyPropertyChanged(BR.numberOfSets)
    }

}

private operator fun ObservableInt.plusAssign(value: Int) {
    set(get() + value)
}

private operator fun ObservableInt.minusAssign(amount: Int) {
    plusAssign(- amount)
}

enum class TimerStates { STOPPED, STARTED, PAUSED}
enum class StartedStages { WORKING, RESTING}