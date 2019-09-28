package com.example.workouttimer.ui

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseMethod
import com.example.workouttimer.R

/*
 getter setter for binding attribute two way
 */

object NumberOfSetsBindingAdapters {

    @BindingAdapter("numberOfSets")
    @JvmStatic fun setNumberOfSets(view: EditText, value: String) {
        view.setText(value)
    }

    @InverseBindingAdapter(attribute = "numberOfSets")
    @JvmStatic fun getNumberOfSets(editText: EditText): String {
        return editText.text.toString()
    }

    @BindingAdapter("numberOfSetsAttrChanged")
    @JvmStatic fun setListener(view: EditText, listener: InverseBindingListener?) {
        view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
            val textView = focusedView as TextView
            if (hasFocus) {
                textView.text = ""
            } else {
                listener?.onChange()
            }
        }
    }
}
/*
    Two way binding converter for attribute NumberOfSets
 */
object NumberOfSetsConverters {

    @InverseMethod("stringToSetArray")
    @JvmStatic fun setArrayToString(context: Context, value: Array<Int>): String {
        return context.getString(R.string.sets_format, value[0] + 1, value[1])
    }
    @JvmStatic fun stringToSetArray(unused: Context, value: String): Array<Int> {
        if (value.isEmpty()) {
            return arrayOf(0, 0)
        }

        return try {
            arrayOf(0, value.toInt())
        } catch (e: NumberFormatException) {
            arrayOf(0, 0)
        }
    }
}
