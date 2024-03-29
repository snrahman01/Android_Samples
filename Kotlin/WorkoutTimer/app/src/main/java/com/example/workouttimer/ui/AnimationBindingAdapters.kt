package com.example.workouttimer.ui

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.workouttimer.R

object AnimationBindingAdapters {
    private const val VERTICAL_BIAS_ANIMATION_DURATION = 900L
    private const val BG_COLOR_ANIMATION_DURATION = 500L

    @BindingAdapter(value = ["animatedBackground", "animatedBackgroundStage"], requireAll = true)
    @JvmStatic fun animateBackground(view: View, timerRunning: Boolean, activeStage: Boolean){
        if(!timerRunning){
            view.setBackgroundColor( ContextCompat.getColor(view.context, R.color.disabledInputColor))
            view.setTag(R.id.hasBeenAnimated, false)
            return
        }

        if(activeStage){
            animateBgColor(view, true)
            view.setTag(R.id.hasBeenAnimated, true)
        } else{
            val hasItBeenAnimated = view.getTag(R.id.hasBeenAnimated) as Boolean

            if(hasItBeenAnimated == true){
                animateBgColor(view, false)
                view.setTag(R.id.hasBeenAnimated, false)

            }
        }
    }

    @BindingAdapter(value=["animateVerticalBias", "animateVerticalBiasStage"],
        requireAll = true)
    @JvmStatic fun animateVerticalBias(view: View, timerRunning: Boolean, activeStage: Boolean) {
        when {
            timerRunning && activeStage -> animateVerticalBias(view, 0.6f) // Workout
            timerRunning && !activeStage -> animateVerticalBias(view, 0.4f) // Rest
            else -> animateVerticalBias(view, 0.5f) // Idle
        }
    }

    private fun animateBgColor(view: View, tint: Boolean) {
        val colorRes = ContextCompat.getColor(view.context, R.color.colorPrimaryLight)
        val color2Res = ContextCompat.getColor(view.context, R.color.disabledInputColor)
        val animator = if (tint)
            ObjectAnimator.ofObject(view,
                "backgroundColor",
                ArgbEvaluator(),
                color2Res,
                colorRes)
        else
            ObjectAnimator.ofObject(view,
                "backgroundColor",
                ArgbEvaluator(),
                colorRes,
                color2Res)
        animator.duration = BG_COLOR_ANIMATION_DURATION
        animator.start()
    }

    private fun animateVerticalBias(view: View, position: Float) {
        val layoutParams: ConstraintLayout.LayoutParams =
            view.layoutParams as ConstraintLayout.LayoutParams
        val animator = ValueAnimator.ofFloat(layoutParams.verticalBias, position)
        animator.addUpdateListener { animation ->
            val newParams: ConstraintLayout.LayoutParams =
                view.layoutParams as ConstraintLayout.LayoutParams
            val animatedValue = animation.animatedValue as Float
            newParams.verticalBias = animatedValue
            view.requestLayout()
        }
        animator.interpolator = DecelerateInterpolator()
        animator.duration = VERTICAL_BIAS_ANIMATION_DURATION
        animator.start()
    }
}