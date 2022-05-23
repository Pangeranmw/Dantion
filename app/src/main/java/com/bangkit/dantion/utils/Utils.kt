package com.bangkit.dantion

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.bangkit.dantion.ui.custom.CustomTextInputLayout
import java.util.*
import kotlin.collections.ArrayList


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

fun getAddress(latitude: Double, longitude: Double, context: Context): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
    if (addresses.isNotEmpty()) {
        return addresses[0].getAddressLine(0)
    }
    return null
}
fun getCity(latitude: Double, longitude: Double, context: Context): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
    return addresses[0].locality
}