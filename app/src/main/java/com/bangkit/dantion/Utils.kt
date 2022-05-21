package com.bangkit.dantion

import android.content.Context
import com.bangkit.dantion.ui.custom.CustomTextInputLayout

fun checkPassword(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> emptyData(text,customTextInputLayout,context)
        text.length in 1..5 -> customTextInputLayout.error = context.getString(R.string.password_less_6)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun checkEmail(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> emptyData(text,customTextInputLayout,context)
        !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches() -> customTextInputLayout.error = context.getString(R.string.email_incorrect)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun checkNumber(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> emptyData(text,customTextInputLayout,context)
        text.length in 1..9 -> customTextInputLayout.error = context.getString(R.string.number_less_10)
        else -> hideTextInputError(customTextInputLayout)
    }
}
fun emptyData(text: String, customTextInputLayout: CustomTextInputLayout, context: Context){
    when{
        text.isEmpty() -> customTextInputLayout.error = context.getString(R.string.empty_form)
        else -> hideTextInputError(customTextInputLayout)
    }
}

fun hideTextInputError(customTextInputLayout: CustomTextInputLayout){
    customTextInputLayout.error = null
    customTextInputLayout.isErrorEnabled = false
}