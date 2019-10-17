package com.example.mykitchen.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @BindingAdapter("android:text")
    @JvmStatic fun setText(view: TextView, id: Int) {
        view.setText(id)
    }
}
